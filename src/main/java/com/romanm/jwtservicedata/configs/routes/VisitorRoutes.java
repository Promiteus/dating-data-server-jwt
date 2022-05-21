package com.romanm.jwtservicedata.configs.routes;

import com.romanm.jwtservicedata.configs.routes.handlers.VisitorRoutesHandler;
import com.romanm.jwtservicedata.constants.Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class VisitorRoutes {

    /**
     * Получить список посетителей пользователя с userId постранично
     * @param visitorRoutesHandler VisitorRoutesHandler
     * @return  RouterFunction<ServerResponse>
     */
    @Bean
    public RouterFunction<ServerResponse> getUserVisitorsRoute(VisitorRoutesHandler visitorRoutesHandler) {
        return route(GET(Api.API_PREFIX+Api.API_USER_VISITOR).and(accept(MediaType.APPLICATION_JSON)), visitorRoutesHandler::getUserVisitors);

    }
}
