package com.romanm.jwtservicedata.services;

import com.romanm.jwtservicedata.components.confs.FileConfig;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.responses.files.FileStatus;
import com.romanm.jwtservicedata.services.abstracts.StorageServiceBase;
import com.romanm.jwtservicedata.services.interfaces.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ZeroCopyHttpOutputMessage;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StorageServiceV1 extends StorageServiceBase implements StorageService {

    private final FileConfig fileConfig;

    @Autowired
    public StorageServiceV1(FileConfig fileConfig) {
        super(fileConfig);
        this.fileConfig = fileConfig;
    }

    /**
     * Извлечь все файлы из каталога пользователя
     * @param userId String
     * @return Flux<String>
     */
    @Override
    public Flux<String> getFiles(String userId) {
        this.listFiles(userId).forEach(file -> {
            log.info(MessageConstants.prefixMsg("Got file: "+file.getName()));
        });
        return Flux.empty();
    }

    /**
     * Получить экземпляр файла из каталога и перевезти его в массив байт
     * @param userId String
     * @param fileName String
     * @return byte[]
     */
    @Override
    public byte[] getFile(String userId, String fileName) {
        List<File> files = this.listFiles(userId)
                .stream()
                .filter(item -> (item.getName().equals(fileName)))
                .collect(Collectors.toList());
        if (files.size() > 0) {
            File file = files.get(0);
            try {
                return Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                log.error(MessageConstants.errorPrefixMsg(e.getMessage()));
            }
        }
        return new byte[]{};
    }

    /**
     * Сохранить файл в каталог пользователя
     * @param userId String
     * @param filePartMono Mono<FilePart>
     * @return Mono<Boolean>
     */
    @Override
    public Mono<FileStatus> save(String userId, Mono<FilePart> filePartMono) {
        return Mono.create(sink -> {
            if (userId != null) {Optional.ofNullable(filePartMono).ifPresent(file -> {
                this.save(file, userId).doOnSuccess(sink::success).subscribe();
            });
            } else {
                sink.success(new FileStatus(false, "", MessageConstants.MSG_NOT_ALL_HTTP_PARAMS));
            }
        });
    }

    /**
     * Сохранение нескольких файлов одновременно
     * @param userId String
     * @param files  Flux<FilePart>
     * @return Flux<FileStatus>
     */
    @Override
    public Flux<FileStatus> saveAll(String userId, Flux<FilePart> files) {
        return this.saveAllFlux(files, userId);
    }

    /**
     * Удалить выбранный файл из каталога пользователя
     * @param userId String
     * @param fileName String
     * @return Mono<Boolean>
     */
    @Override
    public Mono<Boolean> remove(String userId, String fileName) {
        return Mono.create(sink -> {
            if ((userId != null) && (fileName != null)) {
                sink.success(this.deleteUserFile(fileName, userId));
            } else {
                sink.success(false);
            }
        });
    }

    /**
     * Удалить все файлы из каталога пользователя
     * @param userId String
     * @return Mono<Boolean>
     */
    @Override
    public Mono<Boolean> removeAll(String userId) {
        return Mono.create(sink -> {
           if (userId != null) {
               sink.success(this.deleteAll(userId));
           } else {
               sink.success(false);
           }
        });
    }


}
