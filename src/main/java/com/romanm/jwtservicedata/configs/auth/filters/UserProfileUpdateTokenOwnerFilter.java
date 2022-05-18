package com.romanm.jwtservicedata.configs.auth.filters;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.UserProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

/**
 * Класс-фильтр для защиты изменения данных профиля чужим аккаунтом
 */
@Slf4j
public class UserProfileUpdateTokenOwnerFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    /**
     * Фильтр, блокирующий изменение данных пользователя с чужого токена
     * @param request ServerRequest
     * @param next HandlerFunction<ServerResponse>
     * @return Mono<ServerResponse>
     */
    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        Mono<UserProfile> userProfileMono = request.bodyToMono(UserProfile.class);
        String confirmedUserId = request.headers().firstHeader(Api.X_CONFIRMED_UID);

        return userProfileMono.flatMap(body -> {
            log.warn("body profile: "+body.toString());
            if (body.getId() != null) {
                if ((confirmedUserId != null) && confirmedUserId.equals(body.getId())) {
                    return next.handle(request);
                }
                log.error(MessageConstants.prefixMsg(MessageConstants.MSG_INVALID_JWT_OWNER));
                return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, MessageConstants.MSG_INVALID_JWT_OWNER));
            }
            return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, MessageConstants.MSG_INVALID_JWT_OWNER));
        });
    }
}
