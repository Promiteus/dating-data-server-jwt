package com.romanm.jwtservicedata.configs.routes;

import com.romanm.jwtservicedata.configs.auth.filters.UserProfileTokenOwnerFilter;
import com.romanm.jwtservicedata.configs.routes.handlers.UserProfileRoutesHandler;
import com.romanm.jwtservicedata.constants.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
public class UserProfileRoutes {

    @Bean
    @Profile(value = {"dev, prod"})
    public RouterFunction<ServerResponse> profileRoute(UserProfileRoutesHandler userProfileRoutesHandler) {
        return route(GET(Api.API_PREFIX+Api.API_USER_PROFILE_USER_ID).and(accept(MediaType.APPLICATION_JSON)), userProfileRoutesHandler::getUserProfile)
                .filter(new UserProfileTokenOwnerFilter());
    }


    @Bean
    @Profile(value = {"test"})
    public RouterFunction<ServerResponse> testProfileRoute(UserProfileRoutesHandler userProfileRoutesHandler) {
        return route(GET(Api.API_PREFIX+Api.API_USER_PROFILE_USER_ID).and(accept(MediaType.APPLICATION_JSON)),
                userProfileRoutesHandler::getUserProfile);
    }

    @Bean
    public RouterFunction<ServerResponse> pageableProfileRoute(UserProfileRoutesHandler userProfileRoutesHandler) {
        return route(GET(Api.API_PREFIX+Api.API_USER_PROFILES).and(accept(MediaType.APPLICATION_JSON)),
                userProfileRoutesHandler::getUserProfilesByPage);
    }

    @Bean
    @Profile(value = {"dev, prod"})
    public RouterFunction<ServerResponse> updateProfileRoute(UserProfileRoutesHandler userProfileRoutesHandler) {
        return route(POST(Api.API_PREFIX+Api.API_USER_PROFILE).and(accept(MediaType.APPLICATION_JSON)),
                userProfileRoutesHandler::saveUserProfile);
    }

    @Bean
    @Profile(value = {"test"})
    public RouterFunction<ServerResponse> testUpdateProfileRoute(UserProfileRoutesHandler userProfileRoutesHandler) {
        return route(POST(Api.API_PREFIX+Api.API_USER_PROFILE).and(accept(MediaType.APPLICATION_JSON)),
                userProfileRoutesHandler::noCheckSaveUserProfile);
    }

    @Bean
    @Profile(value = {"dev, prod"})
    public RouterFunction<ServerResponse> deleteProfileRoute(UserProfileRoutesHandler userProfileRoutesHandler) {
        return route(DELETE(Api.API_PREFIX+Api.API_USER_PROFILE_USER_ID).and(accept(MediaType.APPLICATION_JSON)),
                userProfileRoutesHandler::removeUserProfile)
               .filter(new UserProfileTokenOwnerFilter());
    }

    @Bean
    @Profile(value = {"test"})
    public RouterFunction<ServerResponse> testDeleteProfileRoute(UserProfileRoutesHandler userProfileRoutesHandler) {
        return route(DELETE(Api.API_PREFIX+Api.API_USER_PROFILE_USER_ID).and(accept(MediaType.APPLICATION_JSON)),
                userProfileRoutesHandler::removeUserProfile);
    }

    @Bean
    public RouterFunction<ServerResponse> searchProfileRoute(UserProfileRoutesHandler userProfileRoutesHandler) {
        return route(POST(Api.API_PREFIX+Api.API_POST_USER_PROFILES).and(accept(MediaType.APPLICATION_JSON)),
                userProfileRoutesHandler::getUserProfilesByPageWithSearchBody);
    }
}
