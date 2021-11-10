package com.romanm.jwtservicedata.services.profile;

import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.Visitor;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import com.romanm.jwtservicedata.repositories.UserProfileRepository;
import com.romanm.jwtservicedata.repositories.VisitorRepository;
import com.romanm.jwtservicedata.services.interfaces.IUserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Service("userProfileServiceV1")
public class UserProfileServiceV1 implements IUserProfileService {

    private UserProfileRepository userProfileRepository;
    private VisitorRepository visitorRepository;

    @Autowired
    public UserProfileServiceV1(UserProfileRepository userProfileRepository, VisitorRepository visitorRepository) {
        this.userProfileRepository = userProfileRepository;
        this.visitorRepository = visitorRepository;
    }

    /**
     * Получить сосnавную сущность профиля пользовтаеля
     * @param userId
     * @return
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
            userProfile.subscribe(profile -> {
                ResponseUserProfile responseUserProfile = new ResponseUserProfile(profile);

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
            });
        });
    }

    /**
     * Сохранить новый профиль или изменить текущий
     * @param userProfile
     * @return
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
     * @param userId
     * @param soft
     * @return
     */
    @Override
    public Mono<Boolean> removeUserProfile(String userId, boolean soft) {
        if (userId == null) {
            return Mono.create(sink -> {
                sink.success(false);
            });
        }
        this.userProfileRepository.removeByUserId(userId);

        return Mono.create(sink -> {
            this.userProfileRepository.findUserProfileByUserId(userId).subscribe(profile -> {
                sink.success(profile == null);
            });
        });
    }

}
