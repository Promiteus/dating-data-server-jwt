package com.romanm.jwtservicedata.configs.auth.filters;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.constants.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;



@Slf4j
public class UserProfileTokenOwnerFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {
    /**
     * Фильтр, блокирующий получение данных пользователя, если была попытка запроса с чужого токена
     * @param request ServerRequest
     * @param next HandlerFunction<ServerResponse>
     * @return Mono<ServerResponse>
     */
    @Override
    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
        String userId = request.pathVariable(Api.PARAM_USER_ID);
        String confirmedUserId = request.headers().firstHeader(Api.X_CONFIRMED_UID);


        if ((confirmedUserId != null) && confirmedUserId.equals(userId)) {
            return next.handle(request);
        }

        log.error(MessageConstants.prefixMsg(MessageConstants.MSG_INVALID_JWT_OWNER));
        return Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, MessageConstants.MSG_INVALID_JWT_OWNER));
    }
}
