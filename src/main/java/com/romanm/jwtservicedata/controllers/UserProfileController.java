package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import com.romanm.jwtservicedata.services.interfaces.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * {
 *     "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb20zODg5QHlhbmRleC5ydSIsImV4cCI6MTYzOTQwMDI2N30.OK2ZNiQ5znQ4fgPBLsLKsSd4CX8S64WemzT4YDVWgXNUAQJUH8RL38roy4BT3VK_6qPw0oLa2pP1o6GY1uGIcQ",
 *     "token_expire_sec": "2021-12-13 T 22:57:47.351",
 *     "user_id": "ff8081817ce58288017ce584265d0000"
 * }*/
@RestController
@RequestMapping(value = Api.API_PREFIX)
public class UserProfileController {

    private final UserProfileService userProfileService;

    /**
     * Конструктор UserProfileController
     * @param userProfileService IUserProfileService
     */
    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    /**
     * Получить профиль пользователя
     * @param userId String
     * @return Mono<ResponseEntity<ResponseUserProfile>>
     */
    @GetMapping(value = Api.API_USER_PROFILE_USER_ID)
    public Mono<ResponseEntity<ResponseUserProfile>> getUserProfile(@PathVariable(Api.PARAM_USER_ID) String userId) {
        return Mono.create(sink -> {
            Mono<ResponseUserProfile> responseUserProfileMono = this.userProfileService.getUserProfile(userId);
            responseUserProfileMono.subscribe(userProfile -> {
                if  (userProfile.getUserProfile() != null) {
                    sink.success(ResponseEntity.ok().body(userProfile));
                } else {
                    sink.success(ResponseEntity.notFound().build());
                }
            });
        });
    }

    /**
     * Создать/изменить профиль пользователя
     * @param userProfile UserProfile
     * @return Mono<ResponseEntity<UserProfile>>
     */
    @PostMapping(value = Api.API_USER_PROFILE)
    public Mono<ResponseEntity<UserProfile>> updateOrSaveUserProfile(@RequestBody UserProfile userProfile) {
        return Mono.create(sink -> {
            this.userProfileService.saveOrUpdateUserProfile(userProfile).doOnSuccess(profile -> {
                if (profile != null) {
                    sink.success(ResponseEntity.accepted().body(profile));
                } else {
                    sink.success(ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
                }
            }).doOnError(error -> {
                sink.success(ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
            }).subscribe();
        });
    }

    /**
     * Удалить профиль пользователя
     * @param userId String
     * @return  Mono<ResponseEntity<Void>>
     */
    @DeleteMapping(value = Api.API_USER_PROFILE_USER_ID)
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
    }

    /**
     * Получить постраничный список профилей пользователей, кроме указанного userId
     * @param page int
     * @return ResponseEntity<Flux<UserProfile>>
     */
    @GetMapping(value = Api.API_USER_PROFILES, produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public ResponseEntity<Flux<UserProfile>> findAllPageble(
            @RequestParam(value = Api.PARAM_PAGE, defaultValue = "0") int page,
            @RequestParam(value = Api.PARAM_NOT_USER_ID, defaultValue = "") String userId) {

        // Flux<UserProfile> userProfileFlux = this.userProfileService.findAllUserProfilesByPage(20, page, userId);

         return ResponseEntity.ok(this.userProfileService.findAllUserProfilesByPage(20, page, userId));
    }
}
