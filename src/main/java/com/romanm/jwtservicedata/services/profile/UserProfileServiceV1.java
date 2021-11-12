package com.romanm.jwtservicedata.services.profile;

import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.Visitor;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import com.romanm.jwtservicedata.repositories.UserProfileRepository;
import com.romanm.jwtservicedata.repositories.VisitorRepository;
import com.romanm.jwtservicedata.services.interfaces.IUserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("userProfileServiceV1")
public class UserProfileServiceV1 implements IUserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final VisitorRepository visitorRepository;

    @Autowired
    public UserProfileServiceV1(UserProfileRepository userProfileRepository, VisitorRepository visitorRepository) {
        this.userProfileRepository = userProfileRepository;
        this.visitorRepository = visitorRepository;
    }

    private void success(MonoSink<ResponseUserProfile> sink, UserProfile profile, Flux<Visitor> visitorFlux) {
        ResponseUserProfile responseUserProfile = new ResponseUserProfile(profile);

        log.info("profile: "+profile);
        visitorFlux.collectList().subscribe(visitors -> {
            if ((visitors != null) && (visitors.size() > 0)) {
                List<String> visitorsIds = visitors.stream().map(Visitor::getVisitorUserId).collect(Collectors.toList());
                this.userProfileRepository.findUserProfilesByUserIdIn(visitorsIds).collectList().subscribe(userProfiles -> {
                    responseUserProfile.getLastVisitors().addAll(userProfiles);
                    //Профиль пользователя с визитерами
                    sink.success(responseUserProfile);
                });
            }
            //Профиль пользователя без визитеров
            sink.success(responseUserProfile);
        });
    }

    /**
     * Получить сосnавную сущность профиля пользовтаеля
     * @param userId String
     * @return Mono<ResponseUserProfile>
     */
    @Override
    public Mono<ResponseUserProfile> getUserProfile(String userId) {

        if (userId == null) {
            return Mono.create(sink -> {
                sink.success(null);
            });
        }

        Mono<UserProfile> userProfile = this.userProfileRepository.findUserProfileByUserId(userId);
        Flux<Visitor> visitorFlux = this.visitorRepository.findVisitorByUserId(userId);

        return Mono.create(sink -> {
            userProfile.doOnSuccess(profile -> {
                this.success(sink, profile, visitorFlux);
             }).subscribe();
         });
    }

    /**
     * Сохранить новый профиль или изменить текущий
     * @param userProfile UserProfile
     * @return  Mono<UserProfile>
     */
    @Override
    public Mono<UserProfile> saveOrUpdateUserProfile(UserProfile userProfile) {
        if (userProfile == null) {
            return Mono.create(sink -> {
                sink.success(null);
            });
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
            this.userProfileRepository.removeUserProfileByUserId(userId).doOnSuccess(cons -> {
                sink.success(cons != null);
            }).subscribe();
        });
    }

}
