package com.romanm.jwtservicedata.configs.auth.filters;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.auth.AuthUser;
import com.romanm.jwtservicedata.services.UserServiceV1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Фильтр для идентификации токена по публичному ключу
 */
@Slf4j
public class JWTAuthorizationFilter implements WebFilter {
    private final UserServiceV1 userService;

    public JWTAuthorizationFilter(UserServiceV1 userService) {
        this.userService = userService;
    }

    /**
     * Метод фильтра, где проверяется токен авторизации
     * @param exchange ServerWebExchange
     * @param chain WebFilterChain
     * @return Mono<Void>
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.getRawStatusCode() == HttpStatus.FORBIDDEN.value()) {
            if (exchange.getRequest().getHeaders().get(MessageConstants.HEADER_STRING) != null) {
                String header = exchange.getRequest().getHeaders().get(MessageConstants.HEADER_STRING).get(0);

                if (header == null || !header.startsWith(MessageConstants.TOKEN_PREFIX)) {
                    return response.setComplete();
                }
                //Здесь проверяется токен JWT
                AuthUser authUser = getAuthentication(exchange);
                if (authUser == null) {
                    return response.setComplete();
                }
            } else {
                MessageConstants.getDecodedUserMsg(exchange.getRequest().getRemoteAddress().getHostString(), exchange.getRequest().getURI().toString(), exchange.getRequest().getMethod().name());
                return response.setComplete();
            }
        } else {
            MessageConstants.getDecodedUserMsg(exchange.getRequest().getRemoteAddress().getHostString(), exchange.getRequest().getURI().toString(), exchange.getRequest().getMethod().name());
        }

        response.setStatusCode(HttpStatus.OK); //Если токен валидный и срок его не истек
        return chain.filter(exchange);
    }


    /**
     * Метод, проверяющий токен авторизации на валидность
     * @param exchange ServerWebExchange
     * @return AuthUser
     */
    private AuthUser getAuthentication(ServerWebExchange exchange) {
        String token = exchange.getRequest().getHeaders().get(MessageConstants.HEADER_STRING).get(0);

        if (token != null) {
            String userName = null;
            try {
                userName = JWT.require(Algorithm.HMAC512(MessageConstants.SECRET.getBytes()))
                        .build()
                        .verify(token.replace(MessageConstants.TOKEN_PREFIX, ""))
                        .getSubject();
            } catch (Exception e) { // Неверный токен или истекло его время действия
                MessageConstants.invalidToken(e.getMessage());
            }

            MessageConstants.getDecodedUserMsg(userName, exchange.getRequest().getURI().toString(), exchange.getRequest().getMethod().name());
            if (userName != null) {
                AuthUser authUser = this.userService.readUserByUsername(userName);

                if (authUser == null) {
                    MessageConstants.getUserNotFoundMsg(userName);
                    return null;
                }

                if (authUser.isDisabled()) {
                    MessageConstants.userBlocked(authUser.getUsername());
                    return null;
                }
                return authUser;
            }
            return null;
        }
        return null;
    }
}
