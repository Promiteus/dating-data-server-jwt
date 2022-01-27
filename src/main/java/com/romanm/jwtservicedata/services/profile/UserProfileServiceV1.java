package com.romanm.jwtservicedata.services.profile;

import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.Visitor;
import com.romanm.jwtservicedata.models.requests.SearchBody;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import com.romanm.jwtservicedata.repositories.UserProfileRepository;
import com.romanm.jwtservicedata.repositories.VisitorRepository;
import com.romanm.jwtservicedata.services.interfaces.UserProfileService;
import com.romanm.jwtservicedata.services.mongodb.MongoOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import java.util.stream.Collectors;

@Slf4j
@Service("userProfileServiceV1")
public class UserProfileServiceV1 implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final VisitorRepository visitorRepository;
    private final MongoOperations mongoOperations;

    /**
     * @param userProfileRepository UserProfileRepository
     * @param visitorRepository VisitorRepository
     * @param mongoOperations MongoOperations
     */
    @Autowired
    public UserProfileServiceV1(UserProfileRepository userProfileRepository,
                                VisitorRepository visitorRepository,
                                MongoOperations mongoOperations) {
        this.userProfileRepository = userProfileRepository;
        this.visitorRepository = visitorRepository;
        this.mongoOperations = mongoOperations;
    }


    /**
     * Получить составную сущность профиля пользовтаеля по идентификатору пользователя - ResponseUserProfile
     * @param userId String
     * @return Mono<ResponseUserProfile>
     */
    @Override
    public Mono<ResponseUserProfile> getUserProfile(String userId) {
        if (userId == null) {
            return Mono.just(new ResponseUserProfile());
        }
        //Получить данные профиля текущего пользователя
        Mono<UserProfile> userProfile = this.userProfileRepository.findUserProfileById(userId);
        Mono<List<UserProfile>> lastVisitors = this.findVisitorsIfProfile(userId);
        Mono<List<UserProfile>> lastChats = this.findChatUserProfilesByPage(userId, 20, 0);

        return Mono.from(Flux.zip(lastVisitors, lastChats, userProfile.map(ResponseUserProfile::new)).map((data) -> {
            ResponseUserProfile responseUserProfile = data.getT3();
            responseUserProfile.getLastVisitors().addAll(data.getT1());
            responseUserProfile.getLastChats().addAll(data.getT2());
            return responseUserProfile;
        })).switchIfEmpty(Mono.just(new ResponseUserProfile()));
    }



    /**
     * Сохранить новый профиль или изменить текущий
     * @param userProfile UserProfile
     * @return  Mono<UserProfile>
     */
    @Override
    public Mono<UserProfile> saveOrUpdateUserProfile(UserProfile userProfile) {
        if (userProfile == null) {
            return Mono.empty();
        }

        return this.userProfileRepository.save(userProfile);
    }

    /**
     * Удалить профиль пользователя
     * @param userId String
     * @param soft boolean
     * @return Mono<Boolean>
     */
    @Override
    public Mono<Boolean> removeUserProfile(String userId, boolean soft) {
        if (userId == null) {
            return Mono.create(sink -> {
                sink.success(false);
            });
        }

        return Mono.create(sink -> {
            this.userProfileRepository.removeUserProfileById(userId).doOnSuccess(cons -> {
                sink.success(cons != null);
            }).subscribe();
        });
    }

    /**
     *
     * @param pageSize int
     * @param page int
     * @param notUserId String
     * @return Flux<UserProfile>
     */
    @Override
    public Flux<UserProfile> findAllUserProfilesByPage(int pageSize, int page, String notUserId) {
        return this.mongoOperations.findAllUserProfilesByPage(page, pageSize, notUserId);
    }

    /**
     * Получить список профилей пользователей по заданным параметрам поиска searchBody
     * @param pageSize int
     * @param page int
     * @param notUserId String
     * @param searchBody SearchBody
     * @return Flux<UserProfile>
     */
    @Override
    public Flux<UserProfile> findAllUserProfilesByPage(int pageSize, int page, String notUserId, SearchBody searchBody) {
        return null;
    }

    /**
     * Найти рофили пользователей по переписке для данного пользователя
     * @param userId String
     * @param pageSize int
     * @param page int
     * @return Flux<UserProfile>
     */
    @Override
    public Mono<List<UserProfile>> findChatUserProfilesByPage(String userId, int pageSize, int page) {
        return this.mongoOperations
                .findDistinctProfileIdOfChat(userId, page, pageSize)
                .collectList()
                .flatMap(s -> this.userProfileRepository.findUserProfilesByIdIn(s).collectList());
    }

    /**
     *
     * @param userId String
     * @return Mono<List<UserProfile>>
     */
    @Override
    public Mono<List<UserProfile>> findVisitorsIfProfile(String userId) {
       return this.mongoOperations
               .findVisitorByUserIdDistinctVisitorUserIdOrderByTimestampDesc(userId, 0, 30)
               .collectList()
               .flatMap(s -> {
                   List<String> visitorsIds = s.stream().map(Visitor::getVisitorUserId).collect(Collectors.toList());
                   return this.userProfileRepository.findUserProfilesByIdIn(visitorsIds).collectList();
               });
    }

}
