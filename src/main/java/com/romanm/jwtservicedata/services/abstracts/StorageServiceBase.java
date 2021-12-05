package com.romanm.jwtservicedata.services.abstracts;

import com.romanm.jwtservicedata.components.confs.FileConfig;
import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.responses.files.FileStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class StorageServiceBase {
    private final FileConfig fileConfig;

    public StorageServiceBase(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    /**
     * Создание каталога
     * @param dir String
     */
    private void createWorkDir(String dir) {
        try {
            Files.createDirectory(Paths.get(dir));
        } catch (IOException e) {
            log.error(MessageConstants.errorPrefixMsg(e.getMessage()));
        }
    }

    /**
     * Проверка существования каталога
     * @param dir String
     * @return boolean
     */
    private boolean isDirExists(String dir) {
        return Files.exists(Paths.get(dir));
    }

    /**
     * Подготовить рабочую директорию для файлов
     * @param fileConfig FileConfig
     * @param userId String
     */
    private void initFilesDirectory(FileConfig fileConfig, String userId) {
        if (!this.isDirExists(fileConfig.getUploadsDir())) {
            this.createWorkDir(fileConfig.getUploadsDir());
        }

        if (!this.isDirExists(String.format(CommonConstants.MULTIMEDIA_DEST_DIR, fileConfig.getUploadsDir(), userId))) {
            this.createWorkDir(String.format(CommonConstants.MULTIMEDIA_DEST_DIR, fileConfig.getUploadsDir(), userId));
        }
    }

    /**
     * Получить число файлов в каталоге
     * @param userId String
     * @return int
     */
    private boolean isFilesLimit(String userId, int maxFilesCount) {
        try (Stream<Path> files = Files.list(Paths.get(String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.fileConfig.getUploadsDir(), userId)))) {
            long filesCount = files.count();
            log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_FILES_COUNT, userId, filesCount)));
            return filesCount > maxFilesCount-1;
        } catch (IOException e) {
            log.error(MessageConstants.errorPrefixMsg(e.getMessage()));
        }
        return false;
    }

    /**
     * Сохранить группу файлов в каталоге пользователя
     * @param files Flux<FilePart>
     * @param userId String
     * @return Mono<Void>
     */
    protected Flux<FileStatus> saveAllFlux(Flux<FilePart> files, String userId) {
        this.initFilesDirectory(this.fileConfig, userId);

        return files.flatMap(filePart -> {
            String fileName = String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.fileConfig.getUploadsDir(), userId)+"/"+filePart.filename();
            return Mono.create(sink -> {
                if (this.isFilesLimit(userId, this.fileConfig.getMaxCount())) {
                    sink.success(new FileStatus(false, fileName, String.format(MessageConstants.MSG_MAX_FILES_COUNT, this.fileConfig.getMaxCount()), ""));
                    return;
                }

                filePart.transferTo(Paths.get(fileName).toFile())
                        .doOnSuccess(s -> {
                            log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_FILE_SAVED_SUCCESSFUL, fileName)));
                            sink.success(new FileStatus(true, fileName, ""));
                        })
                        .doOnError(err -> {
                            log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_ERR_FILE_SAVING, fileName, err.getMessage())));
                            sink.success(new FileStatus(true, fileName, err.getMessage()));
                        }).subscribe();
            });
        });
    }

    /**
     * Сохранить/изменить файл пользователя в каталоге
     * @param file Mono<FilePart>
     * @param userId String
     * @return Mono<Boolean>
     */
    protected Mono<FileStatus> save(Mono<FilePart> file, String userId) {
        this.initFilesDirectory(this.fileConfig, userId);

        return Mono.create(sink -> {
            file.doOnSuccess(filePart -> {
                String fileName = String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.fileConfig.getUploadsDir(), userId)+"/"+filePart.filename();

                if (this.isFilesLimit(userId, this.fileConfig.getMaxCount())) {
                    sink.success(new FileStatus(false, fileName, String.format(MessageConstants.MSG_MAX_FILES_COUNT, this.fileConfig.getMaxCount())));
                    return;
                }


                filePart.transferTo(Paths.get(fileName).toFile()).doOnSuccess(t -> {
                    String msg = String.format(MessageConstants.MSG_FILE_SAVED_SUCCESSFUL, filePart.filename());
                    log.info(MessageConstants.prefixMsg(msg));
                    sink.success(new FileStatus(true, fileName, ""));
                }).doOnError(err -> {
                    String msg = String.format(MessageConstants.MSG_ERR_FILE_SAVING, filePart.filename(), err.getMessage());
                    log.info(MessageConstants.prefixMsg(msg));
                    sink.success(new FileStatus(false, fileName, msg));
                }).subscribe();

            }).doOnError(err -> {
                log.info(MessageConstants.errorPrefixMsg(err.getMessage()));
                sink.success(new FileStatus(false, "", err.getMessage()));
            }).subscribe();
        });
    }

    /**
     * Удалить файл пользователя из каталога
     * @param fileName String
     * @param userId String
     * @return boolean
     */
    protected boolean deleteUserFile(String fileName, String userId) {
        try {
           String file = String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.fileConfig.getUploadsDir(), userId)+"/"+fileName;
           boolean res = Files.deleteIfExists(Paths.get(file));
           if (res) {
               log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_DELETED_FILE_SUCCESSFUL, file)));
           } else {
               log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_CANT_DELETE_FILE, file)));
           }
           return res;
        } catch (IOException e) {
            log.error(MessageConstants.errorPrefixMsg(e.getMessage()));
        }
        return false;
    }

    /**
     * Удалить все файлы из каталога пользователя
     * @param userId String
     * @return boolean
     */
    protected boolean deleteAll(String userId) {
        boolean res = FileSystemUtils.deleteRecursively(Paths.get(String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.fileConfig.getUploadsDir(), userId)).toFile());
        if (res) {
            log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_DELETED_FILES_SUCCESSFUL, userId)));
        } else {
            log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_CANT_DELETE_FILES, userId)));
        }
        return res;
    }

    /**
     * Выдаст список файлов в каталоге пользователя
     * @param userId String
     * @return List<File>
     */
    protected List<File> listFiles(String userId) {
        try (Stream<Path> files = Files.list(Paths.get(String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.fileConfig.getUploadsDir(), userId)))) {
            return files.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            log.error(MessageConstants.errorPrefixMsg(e.getMessage()));
        }
        return List.of();
    }
}
