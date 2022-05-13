package com.romanm.jwtservicedata.configs.auth.filters;

import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.auth.AuthUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

public class UserTokenOwnerFilter implements WebFilter {

    /**
     *
     * @param exchange ServerWebExchange
     * @return boolean
     */
    private boolean confirmUserIdWithToken(ServerWebExchange exchange, List<String> methods, List<String> paths) {
        String path = exchange.getRequest().getPath().value();

        /*Проверяемые методы*/
        boolean hasMethods = methods.stream().filter(item -> (exchange.getRequest().getMethodValue() == item)).count() > 0;
        /*Проверяемые маршруты*/
        boolean hasPaths = paths.stream().filter(item -> (exchange.getRequest().getPath().value().contains(item))).count() > 0;

        if (hasPaths && hasMethods) {
            /*Проверить на наличие заголовок X-API-UID*/
            List<String> X_APIS = exchange.getRequest().getHeaders().get(MessageConstants.X_API_UID);
            List<String> X_CONFIRMS = exchange.getRequest().getHeaders().get(MessageConstants.X_CONFIRMED_UID);
            if ((X_APIS != null) && (X_CONFIRMS != null)) {
                String userId = X_APIS.get(0);
                String confirmedUserId = X_CONFIRMS.get(0);
                return confirmedUserId.trim() == userId.trim(); //Проверить, совпадают ли userId
            }
            return false;
        }

        return true;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();

        response.setStatusCode(HttpStatus.OK);

        return chain.filter(exchange);
    }
}
