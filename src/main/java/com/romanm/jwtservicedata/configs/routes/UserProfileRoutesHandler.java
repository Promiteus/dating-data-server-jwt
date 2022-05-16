package com.romanm.jwtservicedata.configs.routes;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import com.romanm.jwtservicedata.services.interfaces.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;

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
}
