package com.romanm.jwtservicedata.configs.auth.filters;

import com.romanm.jwtservicedata.components.auth.OpenUrlChecker;
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
    private final OpenUrlChecker openUrlChecker;

    /**
     * Конструктор класса OpenedPathsFilter
     * @param openUrlChecker OpenUrlChecker
     */
    public OpenedPathsFilter(OpenUrlChecker openUrlChecker) {
        this.openUrlChecker = openUrlChecker;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        if (this.openUrlChecker.check(exchange.getRequest().getURI().getPath())) {
            //Если текущий url открыт - доступ без токена
            exchange.getResponse().setStatusCode(HttpStatus.ACCEPTED);
        } else {
            //Если текущий url закрыт - доступ по токену
            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
        }

        return chain.filter(exchange);
    }


}
