package com.romanm.jwtservicedata.services;

import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.services.interfaces.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StorageServiceV1 implements StorageService {
    private final Path fileDirectory = Paths.get(CommonConstants.MULTIMEDIA_FILE_DIR);

    private void init() {
        try {
            Files.createDirectory(fileDirectory);
        } catch (IOException e) {
            log.error(MessageConstants.errorPrefixMsg(e.getMessage()));
        }
    }

    @Override
    public Mono<Boolean> save(String userId, Mono<FilePart> filePartMono) {

        return Mono.create(sink -> {
            if (userId != null) {Optional.ofNullable(filePartMono).ifPresent(file -> {
                file.doOnSuccess(filePart -> {

                    sink.success(true);
                }).subscribe();
            });
            } else {
                sink.success(false);
            }
        });
    }

    @Override
    public Mono<Boolean> saveAll(String userId, Mono<List<FilePart>> filesPartMono) {
        return Mono.create(sink -> {
            if (userId != null) {Optional.ofNullable(filesPartMono).ifPresent(files -> {
                files.doOnSuccess(fileParts -> {

                    sink.success(true);
                }).subscribe();
            });
            } else {
                sink.success(false);
            }
        });
    }

    @Override
    public Mono<Boolean> remove(String userId, String imageId) {
        return Mono.create(sink -> {
            if ((userId != null) && (imageId != null)) {
                sink.success(true);
            } else {
                sink.success(false);
            }
        });
    }

    private Mono<Boolean> failedAction() {
        return Mono.create(sink -> {
            sink.success(true);
        });
    }
}
