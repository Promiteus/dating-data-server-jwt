package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.models.UserProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = Api.API_PREFIX)
public class UserProfileController {

    @GetMapping(value = Api.API_USER_PROFILE_USER_ID)
    public Mono<ResponseEntity<UserProfile>> getUserProfile(@PathVariable(Api.PARAM_USER_ID) String userId) {

        return Mono.create(sink -> {
            sink.success();
        });
    }

    @PostMapping(value = Api.API_USER_PROFILE)
    public Mono<ResponseEntity<UserProfile>> updateUserProfile(@RequestBody UserProfile userProfile) {

        return Mono.create(sink -> {
            sink.success();
        });
    }

    @DeleteMapping(value = Api.API_USER_PROFILE_USER_ID)
    public Mono<Void> removeUserProfile(@PathVariable(Api.PARAM_USER_ID) String userId) {

        return Mono.create(sink -> {
            sink.success();
        });
    }
}
