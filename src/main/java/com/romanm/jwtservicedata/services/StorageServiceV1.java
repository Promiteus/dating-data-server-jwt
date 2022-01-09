package com.romanm.jwtservicedata.services;

import com.romanm.jwtservicedata.components.confs.FileConfig;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.responses.files.FileStatus;
import com.romanm.jwtservicedata.repositories.UserProfileRepository;
import com.romanm.jwtservicedata.services.abstracts.StorageServiceBase;
import com.romanm.jwtservicedata.services.interfaces.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.multipart.FilePart;
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
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public StorageServiceV1(FileConfig fileConfig, UserProfileRepository userProfileRepository) {
        super(fileConfig);
        this.fileConfig = fileConfig;
        this.userProfileRepository = userProfileRepository;
    }

    /**
     * Извлечь все файлы из каталога пользователя
     * @param userId String
     * @return Flux<String>
     */
    @Override
    public Flux<String> getFiles(String userId) {
        this.fileConfig.listFiles(userId).forEach(file -> {
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
        List<File> files = this.fileConfig.listFiles(userId)
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
     * Получить миниатюру файла из каталога пользователя
     * @param userId String
     * @return byte[]
     */
    @Override
    public byte[] getFileThumb(String userId) {
        List<File> files = this.fileConfig
                .listFiles(userId+"/"+this.fileConfig.getThumbDir())
                .stream()
                .filter(File::isFile)
                .collect(Collectors.toList());
        if (files.size() > 0) {
            try {
                return Files.readAllBytes(files.get(0).toPath());
            } catch (IOException e) {
                log.error(MessageConstants.errorPrefixMsg(e.getMessage()));
            }
        }

        return new byte[0];
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
                this.save(file, userId).map(fileStatus -> (this.updateImgUrlsOfUserProfile(fileStatus, userId))).doOnSuccess(sink::success).subscribe();
            });
            } else {
                sink.success(new FileStatus(false, "", MessageConstants.MSG_NOT_ALL_HTTP_PARAMS, ""));
            }
        });
    }

    /**
     * Сохранить миниатюру выбранного файла.
     * @param userId String
     * @param fileName String
     * @return Mono<FileStatus>
     */
    @Override
    public Mono<FileStatus> saveThumb(String userId, String fileName) {
        return Mono.create(sink -> {
            if ((userId != null) && (fileName != null)) {
                this.saveFileThumb(userId, fileName).map(fileStatus -> (this.updateImgThumbUrlOfUserProfile(fileStatus, userId))).doOnSuccess(sink::success).subscribe();
            } else {
                sink.success(new FileStatus(false, "", MessageConstants.MSG_NOT_ALL_HTTP_PARAMS, ""));
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

    /**
     * Сохранить миниатюру главного изображения
     * @param fileStatus FileStatus
     * @param userId String
     * @return FileStatus
     */
    private FileStatus updateImgThumbUrlOfUserProfile(FileStatus fileStatus, String userId) {
        this.userProfileRepository
                .findUserProfileById(userId)
                .doOnSuccess(userProfile -> {
                    if (fileStatus.isSaved()) {
                        userProfile.setThumbUrl(fileStatus.getUrl());
                        this.userProfileRepository.save(userProfile).subscribe();
                    }
                }).subscribe();

        return fileStatus;
    }

    /**
     * Дописать в профиль пользователя ссылки на сохраненные изображения
     * @param fileStatus FileStatus
     * @param userId String
     * @return FileStatus
     */
    private FileStatus updateImgUrlsOfUserProfile(FileStatus fileStatus, String userId) {
        this.userProfileRepository
                    .findUserProfileById(userId)
                    .doOnSuccess(userProfile -> {
                        if (fileStatus.isSaved()) {
                            userProfile.getImgUrls().add(fileStatus.getUrl());
                            this.userProfileRepository.save(userProfile).subscribe();
                        }
                     }).subscribe();

        return fileStatus;
    }
}
