package com.romanm.jwtservicedata.configs.routes.handlers;

import com.romanm.jwtservicedata.configs.routes.Routes;
import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.models.Visitor;
import com.romanm.jwtservicedata.services.interfaces.UserProfileService;
import com.romanm.jwtservicedata.services.interfaces.VisitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class VisitorRoutesHandler {
    private final VisitorService visitorService;
    private final UserProfileService userProfileService;

    @Autowired
    public VisitorRoutesHandler(VisitorService visitorService, UserProfileService userProfileService) {
        this.visitorService = visitorService;
        this.userProfileService = userProfileService;
    }

    /**
     * Получить список посетителей пользователя с userId постранично
     * @param serverRequest ServerRequest
     * @return Mono<ServerResponse>
     */
    public Mono<ServerResponse> getUserVisitors(ServerRequest serverRequest) {
        String userId = Optional.ofNullable(Routes.getQueryParam(Api.PARAM_USER_ID, serverRequest)).orElse("0");
        String page = Optional.ofNullable(Routes.getQueryParam(Api.PARAM_PAGE, serverRequest)).orElse("0");
        String pageSize = Optional.ofNullable(Routes.getQueryParam(Api.PARAM_PAGE_SIZE, serverRequest)).orElse("20");

        Flux<Visitor> visitorFlux = this.visitorService.findPagebleVisitorsByUserId(userId, Integer.parseInt(page), Integer.parseInt(pageSize));

        return visitorFlux.collectList().flatMap(visitors -> {
            if (!visitors.isEmpty()) {
                List<String> userIds = visitors.stream().map(Visitor::getVisitorUserId).collect(Collectors.toList());
                return ServerResponse.ok().body(this.userProfileService.getUserProfiles(userIds), Visitor.class);
            } else {
                return ServerResponse.notFound().build();
            }
        });
    }
}
