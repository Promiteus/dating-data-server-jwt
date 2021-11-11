package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import com.romanm.jwtservicedata.services.interfaces.IUserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = Api.API_PREFIX)
public class UserProfileController {

    private final IUserProfileService userProfileService;

    @Autowired
    public UserProfileController(IUserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping(value = Api.API_USER_PROFILE_USER_ID)
    public Mono<ResponseEntity<ResponseUserProfile>> getUserProfile(@PathVariable(Api.PARAM_USER_ID) String userId) {
        return Mono.create(sink -> {
            Mono<ResponseUserProfile> responseUserProfileMono = this.userProfileService.getUserProfile(userId);
            responseUserProfileMono.subscribe(userProfile -> {
                if  (userProfile != null) {
                    sink.success(ResponseEntity.ok().body(userProfile));
                } else {
                    sink.success(ResponseEntity.notFound().build());
                }
            });
            //sink.success(ResponseEntity.notFound().build());
        });
    }

    @PostMapping(value = Api.API_USER_PROFILE)
    public Mono<ResponseEntity<UserProfile>> updateUserProfile(@RequestBody UserProfile userProfile) {

        Mono<UserProfile> userProfileMono = this.userProfileService.saveOrUpdateUserProfile(userProfile);

        return Mono.create(sink -> {
            userProfileMono.subscribe(profile -> {
                if (profile != null) {
                    sink.success(ResponseEntity.accepted().build());
                } else {
                    sink.success(ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
                }
            });
        });
    }

    @DeleteMapping(value = Api.API_USER_PROFILE_USER_ID)
    public Mono<ResponseEntity<Void>> removeUserProfile(@PathVariable(Api.PARAM_USER_ID) String userId) {

        Mono<Boolean> removedMono = this.userProfileService.removeUserProfile(userId, false);

        return Mono.create(sink -> {
            removedMono.subscribe(res -> {
                if (res) {
                    sink.success(ResponseEntity.accepted().build()) ;
                } else {
                    sink.success(ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
                }
            });
        });
    }
}
