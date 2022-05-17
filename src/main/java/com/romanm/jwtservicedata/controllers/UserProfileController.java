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


/**
 * {
 *     "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb20zODg5QHlhbmRleC5ydSIsImV4cCI6MTYzOTQwMDI2N30.OK2ZNiQ5znQ4fgPBLsLKsSd4CX8S64WemzT4YDVWgXNUAQJUH8RL38roy4BT3VK_6qPw0oLa2pP1o6GY1uGIcQ",
 *     "token_expire_sec": "2021-12-13 T 22:57:47.351",
 *     "user_id": "ff8081817ce58288017ce584265d0000"
 * }*/
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
     * Удалить профиль пользователя
     * @param userId String
     * @return  Mono<ResponseEntity<Void>>
     */
   /* @DeleteMapping(value = Api.API_USER_PROFILE_USER_ID)
    public Mono<ResponseEntity<Void>> removeUserProfile(@PathVariable(Api.PARAM_USER_ID) String userId) {
        return Mono.create(sink -> {
            this.userProfileService.removeUserProfile(userId, false).subscribe(res -> {
                if (res) {
                    sink.success(ResponseEntity.accepted().build()) ;
                } else {
                    sink.success(ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
                }
            });
        });
    }*/

    /**
     * Получить постраничный список профилей пользователей, кроме указанного userId
     * @param page int
     * @return ResponseEntity<Flux<UserProfile>>
     */
    @GetMapping(value = Api.API_USER_PROFILES, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public ResponseEntity<Flux<UserProfile>> findAllPageble(
            @RequestParam(value = Api.PARAM_PAGE, defaultValue = "0") int page,
            @RequestParam(value = Api.PARAM_NOT_USER_ID, defaultValue = "") String userId) {

         return ResponseEntity.ok(this.userProfileService.findAllUserProfilesByPage(30, page, userId));
    }

    /**
     * Получить список профилей пользователей по заданным параметрам поиска searchBody
     * @param page int
     * @param userId String
     * @param searchBody SearchBody
     * @return ResponseEntity<Flux<UserProfile>>
     */
    @PostMapping(value = Api.API_POST_USER_PROFILES)
    public ResponseEntity<Flux<UserProfile>> findAllProfilesPageableWithSearchBody(
            @PathVariable(value = Api.PARAM_PAGE) int page,
            @PathVariable(value = Api.PARAM_NOT_USER_ID) String userId,
            @RequestBody SearchBody searchBody) {

        return ResponseEntity.ok(this.userProfileService.findAllUserProfilesByPage(30, page, userId, searchBody));
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
