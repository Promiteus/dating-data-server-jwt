package com.romanm.jwtservicedata.configs.auth.filters;

import com.romanm.jwtservicedata.constants.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Класс фильтр, определяющий открытые конечные точки
 */
@Slf4j
public class OpenedPathsFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info(MessageConstants.prefixMsg("OpenedPathsFilter called! "));


        exchange.getResponse().setStatusCode(HttpStatus.ACCEPTED);
        return chain.filter(exchange);
    }
}
