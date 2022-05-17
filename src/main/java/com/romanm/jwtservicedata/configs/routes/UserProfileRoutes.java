package com.romanm.jwtservicedata.configs.routes;

import com.romanm.jwtservicedata.configs.auth.filters.UserTokenOwnerFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
public class UserProfileRoutes {

    @Bean
    public RouterFunction<ServerResponse> profileRoutes(UserProfileRoutesHandler userProfileRoutesHandler) {
        return route(GET("/api/v2/user/profile/{user_id}").and(accept(MediaType.APPLICATION_JSON)), userProfileRoutesHandler::getUserProfile)
                .filter(new UserTokenOwnerFilter());
    }
}
