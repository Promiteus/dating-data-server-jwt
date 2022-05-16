package com.romanm.jwtservicedata.configs.routes;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.constants.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
public class UserProfileRoutes {

    @Bean
    public RouterFunction<ServerResponse> profileRoutes(UserProfileRoutesHandler userProfileRoutesHandler) {
        return route(GET("/api/v2/user/profile/{user_id}").and(accept(MediaType.APPLICATION_JSON)), userProfileRoutesHandler::getUserProfile)
                .filter(new HandlerFilterFunction<ServerResponse, ServerResponse>() {
                    @Override
                    public Mono<ServerResponse> filter(ServerRequest request, HandlerFunction<ServerResponse> next) {
                        log.warn("path variable: "+request.pathVariable(Api.PARAM_USER_ID));
                        log.warn("X_CONFIRM header param: "+request.headers().toString());
                        return next.handle(request);
                    }
                });
    }
}
