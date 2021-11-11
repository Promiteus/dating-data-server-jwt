package com.romanm.jwtservicedata.configs.auth;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.auth.AuthUser;
import com.romanm.jwtservicedata.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;



/**
 * Фильтр для идентификации токена по публичному ключу
 */
public class JWTAuthorizationFilter implements WebFilter {
    private final UserService userService;

    public JWTAuthorizationFilter(UserService userService) {
        this.userService = userService;
    }


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.FORBIDDEN);

        if (exchange.getRequest().getHeaders().get(MessageConstants.HEADER_STRING) != null) {
            String header = exchange.getRequest().getHeaders().get(MessageConstants.HEADER_STRING).get(0);

            if (header == null || !header.startsWith(MessageConstants.TOKEN_PREFIX)) {
                return response.setComplete();
            }

            AuthUser authUser = getAuthentication(exchange);
            if (authUser == null) {
                return response.setComplete();
            }
        } else {
            MessageConstants.getDecodedUserMsg(null, exchange.getRequest().getURI().toString(), exchange.getRequest().getMethod().name());
            return response.setComplete();
        }

        response.setStatusCode(HttpStatus.OK); //Если токен валидный и срок его не истек
        return chain.filter(exchange);
    }



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
