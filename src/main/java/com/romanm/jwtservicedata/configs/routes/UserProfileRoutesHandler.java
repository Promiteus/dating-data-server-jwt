package com.romanm.jwtservicedata.configs.routes;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.requests.SearchBody;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import com.romanm.jwtservicedata.services.interfaces.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.List;
import java.util.Optional;

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
        String confirmedUserId = serverRequest.headers().firstHeader(Api.X_CONFIRMED_UID);

        return body.flatMap(userProfile -> {
             if (userProfile == null) {
                 return ServerResponse
                         .status(HttpStatus.BAD_REQUEST)
                         .build();
             } else {
                 if ((confirmedUserId != null) && confirmedUserId.equals(userProfile.getId())) {
                     return ServerResponse
                             .accepted()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(fromProducer(this.save(userProfile), UserProfile.class));
                 } else {
                     log.warn(MessageConstants.prefixMsg(MessageConstants.MSG_INVALID_JWT_OWNER));
                     return ServerResponse
                             .status(HttpStatus.FORBIDDEN)
                             .build();
                 }
             }
        });
    }

    /**
     * Удалить профиль пользователя
     * @param serverRequest Mono<ServerResponse>
     * @return Mono<ServerResponse>
     */
    public Mono<ServerResponse> removeUserProfile(ServerRequest serverRequest) {
        Mono<Boolean> removed = this.userProfileService.removeUserProfile(serverRequest.pathVariable(Api.PARAM_USER_ID), false);

        return removed.flatMap(res -> {
            if (res) {
                return ServerResponse.accepted().build();
            } else {
                return ServerResponse.status(HttpStatus.NOT_MODIFIED).build();
            }
        });
    }

    /**
     * Получить постраничный список профилей пользователей, кроме указанного userId
     * @param serverRequest ServerRequest
     * @return Mono<ServerResponse>
     */
    public Mono<ServerResponse> getUserProfilesByPage(ServerRequest serverRequest) {
        String page = "0";
        page = this.getQueryParam(Api.PARAM_PAGE, serverRequest);

        String notUserId = "0";
        notUserId = this.getQueryParam(Api.PARAM_NOT_USER_ID, serverRequest);

        Flux<UserProfile> userProfileFlux = this.userProfileService.findAllUserProfilesByPage(30, Integer.parseInt(page), notUserId);

        return userProfileFlux.collectList().flatMap(userProfiles -> {
            return ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromObject(userProfiles));
        });
    }

    /**
     * Получить список профилей пользователей по заданным параметрам поиска searchBody
     * @param serverRequest ServerRequest
     * @return Mono<ServerResponse>
     */
    public Mono<ServerResponse> getUserProfilesByPageWithSearchBody(ServerRequest serverRequest) {
        String page = "0";
        page = serverRequest.pathVariable(Api.PARAM_PAGE);

        String notUserId = "0";
        notUserId = serverRequest.pathVariable(Api.PARAM_NOT_USER_ID);

        Mono<SearchBody> body = serverRequest.bodyToMono(SearchBody.class);

        String finalPage = page;
        String finalNotUserId = notUserId;

        Mono<List<UserProfile>> userProfileFlux = body.flatMap(searchBody -> {
            return this.userProfileService.findAllUserProfilesByPage(30, Integer.parseInt(finalPage), finalNotUserId, searchBody).collectList();
        });

        return userProfileFlux.flatMap(userProfiles -> {
            return ServerResponse
                    .ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromObject(userProfiles));
        });
    }

    /**
     * Получить query параметр из объекта ServerRequest
     * @param paramName String
     * @param serverRequest ServerRequest
     * @return String
     */
    private String getQueryParam(String paramName, ServerRequest serverRequest) {
        String value = "0";
        Optional<String> optValue = serverRequest.queryParam(paramName);
        if (optValue.isPresent()) {
            value = optValue.get();
        }
        return value;
    }

    /**
     * (Действие) Создать/изменить профиль пользователя
     * @param userProfile UserProfile
     * @return Mono<UserProfile>
     */
    private Mono<UserProfile> save(UserProfile userProfile) {

        log.warn("body is: "+userProfile.toString());
        return Mono.fromSupplier(() -> {
            this.userProfileService.saveOrUpdateUserProfile(userProfile).subscribe();
            return userProfile;
        });
    }
}
