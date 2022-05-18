package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.requests.SearchBody;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import com.romanm.jwtservicedata.services.interfaces.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(value = Api.API_PREFIX)
public class UserProfileController {

    private final UserProfileService userProfileService;

    /**
     * Конструктор класса UserProfileController
     * @param userProfileService UserProfileService
     */
    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }


    /**
     * Найти все чаты профиля пользователя по userId
     * @param page int
     * @param userId String
     * @return ResponseEntity<Flux<UserProfile>>
     */
    @GetMapping(value = Api.API_CHAT_USER_PROFILES)
    public ResponseEntity<Mono<List<UserProfile>>> findChatUserProfilesByPage(
           @RequestParam(value = Api.PARAM_PAGE, defaultValue = "0") int page,
           @RequestParam(value = Api.PARAM_PAGE_SIZE, defaultValue = "20") int pageSize,
           @RequestParam(value = Api.PARAM_USER_ID, defaultValue = "") String userId) {

        return ResponseEntity.ok(this.userProfileService.findChatUserProfilesByPage(userId, pageSize, page));
    }
}
