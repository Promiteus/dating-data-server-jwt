package com.romanm.jwtservicedata.services;

import com.romanm.jwtservicedata.components.confs.FileConfig;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.responses.files.FileStatus;
import com.romanm.jwtservicedata.services.abstracts.StorageServiceBase;
import com.romanm.jwtservicedata.services.interfaces.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class StorageServiceV1 extends StorageServiceBase implements StorageService {

    private final FileConfig fileConfig;

    @Autowired
    public StorageServiceV1(FileConfig fileConfig) {
        super(fileConfig.getUploadsDir());
        this.fileConfig = fileConfig;
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
                this.save(file, userId, this.fileConfig.getMaxCount()).doOnSuccess(sink::success).subscribe();
            });
            } else {
                sink.success(new FileStatus(false, "", MessageConstants.MSG_NOT_ALL_HTTP_PARAMS));
            }
        });
    }

    /**
     * Сохранить файлы (список) в каталог пользователя
     * @param userId String
     * @param files List<FilePart>
     * @return Mono<Boolean>
     */
    @Override
    public Mono<Boolean> saveAll(String userId, List<FilePart> files) {
        return this.saveAll(files, userId, this.fileConfig.getMaxCount());
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
