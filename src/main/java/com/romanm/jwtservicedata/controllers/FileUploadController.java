package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.services.interfaces.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = Api.API_PREFIX)
public class FileUploadController {
    @Autowired
    private StorageService storageService;

    /**
     * Получить все изображения пользователя по userId
     * @param userId String
     * @return Mono<ResponseEntity<Flux<Resource>>>
     */
    @GetMapping(value = Api.API_USER_IMAGES_USER_ID)
    public Mono<ResponseEntity<Flux<Resource>>> getImages(@RequestParam(value = Api.PARAM_USER_ID, defaultValue = "", required = true) String userId) {

        return Mono.create(sink -> {
            sink.success(ResponseEntity.ok().build());
        });
    }

    /**
     * Сохранить файл
     * @param userId String
     * @param file Mono<FilePart>
     * @return Mono<ResponseEntity<?>>
     */
    @PostMapping(value = Api.API_USER_IMAGES, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<?>> saveImage(
            @RequestPart(value = Api.PARAM_USER_ID, required = true) String userId,
            @RequestPart(value = Api.PARAM_FILE, required = true) Mono<FilePart> file) {

        log.info("Saving new file! userId: "+userId+" file: "+file.block());

        return Mono.create(sink -> {
            this.storageService.save(userId, file).doOnSuccess(res -> {
                sink.success(res ? ResponseEntity.ok().build(): ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
            }).subscribe();
        });
    }

    /**
     * Изменить выбранное изображение
     * @param userId String
     * @param imageId String
     * @param file Mono<FilePart>
     * @return Mono<ResponseEntity<?>>
     */
    @PutMapping(value = Api.API_USER_IMAGES)
    public Mono<ResponseEntity<?>> updateImage(
            @RequestPart(value = Api.PARAM_USER_ID, required = true) String userId,
            @RequestPart(value = Api.PARAM_IMAGE_ID, required = true) String imageId,
            @RequestPart(value = Api.PARAM_FILE, required = true) Mono<FilePart> file) {

        log.info("Updating file! userId: "+userId+" imageId: "+imageId+" file: "+file.block());

        return Mono.create(sink -> {
            sink.success(ResponseEntity.accepted().build());
        });
    }

    /**
     * Сохранить несколько изображений за раз
     * @param userId String
     * @param files Mono<FilePart>
     * @return Mono<ResponseEntity<?>>
     */
    @PostMapping(value = Api.API_USER_IMAGES_SOME_USER_ID)
    public Mono<ResponseEntity<?>> saveImages(
            @RequestPart(value = Api.PARAM_USER_ID, required = true) String userId,
            @RequestPart(value = Api.PARAM_FILES, required = true) Mono<List<FilePart>> files) {

        log.info("Saving files! userId: "+userId+" files: "+files.block().size());

        return Mono.create(sink -> {
            sink.success(ResponseEntity.ok().build());
        });
    }
}
