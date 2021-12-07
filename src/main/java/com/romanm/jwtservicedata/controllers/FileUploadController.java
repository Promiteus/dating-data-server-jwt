package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.components.http.MediaTypeConvertor;
import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.services.interfaces.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(value = Api.API_PREFIX)
public class FileUploadController {
    @Autowired
    private StorageService storageService;
    @Autowired
    private MediaTypeConvertor mediaTypeConvertor;

    /**
     * Получить файл изображения по ссылке
     * @param userId String
     * @return  Mono<ResponseEntity<byte[]>>
     */
    @GetMapping(value = Api.API_USER_IMAGE)
    public Mono<ResponseEntity<byte[]>> getFile(
            @RequestParam(value = Api.PARAM_USER_ID, defaultValue = "") String userId,
            @RequestParam(value = Api.PARAM_FILE_ID, defaultValue = "") String fileName) {

        //Найти файл пользователя по имени
        byte[] bytes = this.storageService.getFile(userId, fileName);
        //Получить медиатип по названию файла
        String mediaType = this.mediaTypeConvertor.getFileMediaType(fileName);
        if (mediaType == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        //Присвоить медиатип заголовку ответа сервера
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", mediaType);

        return Mono.create(sink -> {
            sink.success(bytes.length > 0 ? new ResponseEntity<>(bytes, headers, HttpStatus.OK): ResponseEntity.notFound().build());
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
            @RequestPart(value = Api.PARAM_USER_ID) String userId,
            @RequestPart(value = Api.PARAM_FILE) Mono<FilePart> file) {

        return Mono.create(sink -> {
            this.storageService.save(userId, file).doOnSuccess(res -> {
                sink.success(ResponseEntity.ok(res));
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
            @RequestPart(value = Api.PARAM_USER_ID) String userId,
            @RequestPart(value = Api.PARAM_FILE_ID) String fileName) {

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
    public Mono<ResponseEntity<?>> deleteFiles(@RequestPart(value = Api.PARAM_USER_ID) String userId) {
        return Mono.create(sink -> {
            this.storageService.removeAll(userId).doOnSuccess(res -> {
                sink.success(res ? ResponseEntity.accepted().build(): ResponseEntity.status(HttpStatus.NOT_MODIFIED).build());
            }).subscribe();
        });
    }

    /**
     * Сохранить группу файлов
     * @param userId String
     * @param files Flux<FilePart>
     * @return Mono<ResponseEntity<?>>
     */
    @PostMapping(value = Api.API_USER_IMAGES_MULTI)
    public Mono<ResponseEntity<?>> saveFluxFiles(
            @RequestPart(value = Api.PARAM_USER_ID) String userId,
            @RequestPart(value = Api.PARAM_FILES) Flux<FilePart> files) {

        return Mono.create(sink -> {
            this.storageService.saveAll(userId, files)
                    .collectList()
                    .doOnSuccess(res -> {
                        sink.success(ResponseEntity.ok(res));
                    }).subscribe();
        });
    }
}
