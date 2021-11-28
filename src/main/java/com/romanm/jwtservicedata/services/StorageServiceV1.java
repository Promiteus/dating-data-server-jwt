package com.romanm.jwtservicedata.services;

import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.services.abstracts.StorageServiceBase;
import com.romanm.jwtservicedata.services.interfaces.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StorageServiceV1 extends StorageServiceBase implements StorageService {

    public StorageServiceV1() {
        super(CommonConstants.MULTIMEDIA_FILE_DIR);
    }

    @Override
    public Mono<Boolean> save(String userId, Mono<FilePart> filePartMono) {
        return Mono.create(sink -> {
            if (userId != null) {Optional.ofNullable(filePartMono).ifPresent(file -> {
                this.save(file).doOnSuccess(sink::success).subscribe();
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

                sink.success(true);
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
