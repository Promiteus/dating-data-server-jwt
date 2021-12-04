package com.romanm.jwtservicedata.services.interfaces;

import com.romanm.jwtservicedata.models.responses.files.FileStatus;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface StorageService {
    Mono<FileStatus> save(String userId, Mono<FilePart> filePartMono);
    Mono<Boolean> saveAll(String userId, List<FilePart> files);
    Mono<Boolean> remove(String userId, String imageId);
    Mono<Boolean> removeAll(String userId);
}
