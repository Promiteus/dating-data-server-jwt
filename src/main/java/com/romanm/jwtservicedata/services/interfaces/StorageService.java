package com.romanm.jwtservicedata.services.interfaces;

import com.romanm.jwtservicedata.models.responses.files.FileStatus;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface StorageService {
    Flux<Resource> getFiles(String userId);
    Mono<FileStatus> save(String userId, Mono<FilePart> filePartMono);
    Flux<FileStatus> saveAll(String userId, Flux<FilePart> files);
    Mono<Boolean> remove(String userId, String imageId);
    Mono<Boolean> removeAll(String userId);
}
