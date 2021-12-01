package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.services.interfaces.StorageService;
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
     * Сохранить/изменить файл
     * @param userId String
     * @param file Mono<FilePart>
     * @return Mono<ResponseEntity<?>>
     */
    @PostMapping(value = Api.API_USER_IMAGES, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<?>> saveFile(
            @RequestPart(value = Api.PARAM_USER_ID, required = true) String userId,
            @RequestPart(value = Api.PARAM_FILE, required = true) Mono<FilePart> file) {

        return Mono.create(sink -> {
            this.storageService.save(userId, file).doOnSuccess(res -> {
                sink.success(res ? ResponseEntity.ok().build(): ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
            }).subscribe();
        });
    }

    /**
     * Удалить выбранный файл
     * @param userId String
     * @param fileName String
     * @return Mono<ResponseEntity<?>>
     */
    @DeleteMapping(value = Api.API_USER_IMAGES, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<?>> deleteFile(
            @RequestPart(value = Api.PARAM_USER_ID, required = true) String userId,
            @RequestPart(value = Api.PARAM_FILE_ID, required = true) String fileName) {

        return Mono.create(sink -> {
            this.storageService.remove(userId, fileName).doOnSuccess(res -> {
                sink.success(res ? ResponseEntity.accepted().build(): ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
            }).subscribe();
        });
    }

    /**
     * Удалить все файлы (изображения) каталога пользователя
     * @param userId String
     * @return Mono<ResponseEntity<?>>
     */
    @DeleteMapping(value = Api.API_USER_IMAGES_ALL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<?>> deleteFiles(@RequestPart(value = Api.PARAM_USER_ID, required = true) String userId) {
        return Mono.create(sink -> {
            this.storageService.removeAll(userId).doOnSuccess(res -> {
                sink.success(res ? ResponseEntity.accepted().build(): ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
            }).subscribe();
        });
    }

    /**
     * Сохранить несколько изображений за раз
     * @param userId String
     * @param files List<FilePart>
     * @return Mono<ResponseEntity<?>>
     */
    @PostMapping(value = Api.API_USER_IMAGES_ALL)
    public Mono<ResponseEntity<?>> saveFiles(
            @RequestPart(value = Api.PARAM_USER_ID, required = true) String userId,
            @RequestPart(value = Api.PARAM_FILES, required = true) List<FilePart> files) {

        return Mono.create(sink -> {
            this.storageService.saveAll(userId, files).doOnSuccess(s -> {
                sink.success(ResponseEntity.ok().build());
            }).subscribe();
        });
    }
}
