package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.constants.Api;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = Api.API_PREFIX)
public class ImageController {

    @GetMapping(value = Api.API_USER_IMAGES_USER_ID)
    public Mono<ResponseEntity<Resource>> getImages(@RequestParam(value = Api.PARAM_USER_ID, defaultValue = "", required = true) String userId) {
        return Mono.empty();
    }

    @PostMapping(value = Api.API_USER_IMAGES)
    public Mono<ResponseEntity<?>> saveImage(
            @RequestParam(value = Api.PARAM_USER_ID, defaultValue = "", required = true) String userId,
            @RequestParam(value = Api.PARAM_IMAGE_ID, defaultValue = "", required = true) String imageId,
            @RequestParam(value = Api.PARAM_FILE, required = true) MultipartFile file) {

        return Mono.empty();
    }

    @PutMapping(value = Api.API_USER_IMAGES_USER_ID)
    public Mono<ResponseEntity<?>> updateImage(
            @RequestParam(value = Api.PARAM_USER_ID, defaultValue = "", required = true) String userId,
            @RequestParam(value = Api.PARAM_IMAGE_ID, defaultValue = "", required = true) String imageId,
            @RequestParam(value = Api.PARAM_FILE, required = true) MultipartFile file) {

        return Mono.empty();
    }

    @PostMapping(value = Api.API_USER_IMAGES_SOME_USER_ID)
    public Mono<ResponseEntity<?>> saveImages(
            @RequestParam(value = Api.PARAM_USER_ID, defaultValue = "", required = true) String userId,
            @RequestParam(value = Api.PARAM_FILES, required = true) MultipartFile[] files) {

       return Mono.empty();
    }
}
