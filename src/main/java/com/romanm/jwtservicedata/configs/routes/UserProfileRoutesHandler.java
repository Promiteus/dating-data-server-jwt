package com.romanm.jwtservicedata.configs.routes;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import com.romanm.jwtservicedata.services.interfaces.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.BodyInserters.fromProducer;

@Slf4j
@Component
public class UserProfileRoutesHandler {
    private final UserProfileService userProfileService;

    /**
     * Конструктор класса UserProfileRoutesHandler
     * @param userProfileService UserProfileService
     */
    @Autowired
    public UserProfileRoutesHandler(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    /**
     * Получить профиль пользователя
     * @param serverRequest ServerRequest
     * @return  Mono<ServerResponse>
     */
    public Mono<ServerResponse> getUserProfile(ServerRequest serverRequest) {
        Mono<ResponseUserProfile> responseUserProfileMono = this.userProfileService.getUserProfile(serverRequest.pathVariable(Api.PARAM_USER_ID));
        return responseUserProfileMono
                .flatMap(responseUserProfile -> {
                    if ((responseUserProfile != null) && (responseUserProfile.getUserProfile() != null)) {
                        return ServerResponse
                                .ok().contentType(MediaType.APPLICATION_JSON)
                                .body(fromObject(responseUserProfile));
                    } else {
                        return ServerResponse.notFound().build();
                    }
                });
    }

    /**
     * Создать/изменить профиль пользователя
     * @param serverRequest ServerRequest
     * @return Mono<ServerResponse>
     */
    public Mono<ServerResponse> saveUserProfile(ServerRequest serverRequest) {
        Mono<UserProfile> body = serverRequest.bodyToMono(UserProfile.class);

        return ServerResponse
                .accepted()
                .contentType(MediaType.APPLICATION_JSON)
                .body(fromProducer(body.flatMap(this::save), UserProfile.class));
    }

    /**
     * (Действие) Создать/изменить профиль пользователя
     * @param userProfile UserProfile
     * @return Mono<UserProfile>
     */
    private Mono<UserProfile> save(UserProfile userProfile) {
        return Mono.fromSupplier(() -> {
            this.userProfileService.saveOrUpdateUserProfile(userProfile).subscribe();
            return userProfile;
        });
    }
}
