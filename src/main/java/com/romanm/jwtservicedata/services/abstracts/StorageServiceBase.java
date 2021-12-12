package com.romanm.jwtservicedata.services.abstracts;

import com.romanm.jwtservicedata.components.confs.FileConfig;
import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.responses.files.FileStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.MonoSink;

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
     * Подготовить рабочие директории для файлов
     * @param fileConfig FileConfig
     * @param userId String
     */
    private String initFilesDirectory(FileConfig fileConfig, String userId, boolean isThumb) {
        if (!this.isDirExists(fileConfig.getUploadsDir())) {
            this.createWorkDir(fileConfig.getUploadsDir());
        }

        String userDirPath = String.format(CommonConstants.MULTIMEDIA_DEST_DIR, fileConfig.getUploadsDir(), userId);
        if (!this.isDirExists(userDirPath)) {
            this.createWorkDir(userDirPath);
        }

        String thumbDirPath = userDirPath+"/"+fileConfig.getThumbDir();
        if (!this.isDirExists(thumbDirPath)) {
            this.createWorkDir(thumbDirPath);
        }

        return isThumb ? thumbDirPath : userDirPath;
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
        this.initFilesDirectory(this.fileConfig, userId, false);

        return files.flatMap(filePart -> {
            String fileName = String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.fileConfig.getUploadsDir(), userId)+"/"+filePart.filename();
            return Mono.create(sink -> {
                if (this.isFilesLimit(userId, this.fileConfig.getMaxCount())) {
                    sink.success(new FileStatus(false, fileName, String.format(MessageConstants.MSG_MAX_FILES_COUNT, this.fileConfig.getMaxCount()), ""));
                    return;
                }

                if (!this.fileConfig.isFileFormatConfirmed(sink, filePart)) {
                    return;
                }

                this.saveFileItem(sink, filePart, fileName, userId);
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
        this.initFilesDirectory(this.fileConfig, userId, false);

        return Mono.create(sink -> {
            file.doOnSuccess(filePart -> {
                String fileName = String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.fileConfig.getUploadsDir(), userId)+"/"+filePart.filename();

                if (!this.fileConfig.isFileFormatConfirmed(sink, filePart)) {
                    return;
                }

                if (this.isFilesLimit(userId, this.fileConfig.getMaxCount())) {
                    sink.success(new FileStatus(false, fileName, String.format(MessageConstants.MSG_MAX_FILES_COUNT, this.fileConfig.getMaxCount())));
                    return;
                }

                this.saveFileItem(sink, filePart, fileName, userId);

            }).doOnError(err -> {
                log.info(MessageConstants.errorPrefixMsg(err.getMessage()));
                sink.success(new FileStatus(false, "", err.getMessage()));
            }).subscribe();
        });
    }

    /**
     * Сохранить изобраение в уменьшенном виде.
     * @param fileName String
     * @param userId String
     * @return Mono<FileStatus>
     */
    protected Mono<FileStatus> saveFileThumb(String fileName, String userId) {
        String thumbDir = this.initFilesDirectory(this.fileConfig, userId, true);
        log.info(MessageConstants.prefixMsg("thumbDir: "+thumbDir));
        /*
        * Thumbnails.of("path/to/image")
          .size(100, 100)
          .toFile("path/to/thumbnail");*/
        return Mono.create(sink -> {
            sink.success(new FileStatus(false, "", ""));
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

    /**
     * Асинхронное сохранение файла с передачей синхронизатора
     * @param sink MonoSink<FileStatus>
     * @param filePart FilePart
     * @param fileName String
     */
    private void saveFileItem(MonoSink<FileStatus> sink, FilePart filePart, String fileName, String userId) {
        //  /api/resource?user_id=208&file_id=ford_mustang_ford_avtomobil_226678_1280x1024.jpg
        String resourceUri = String.format(Api.API_RESOURCE_URI_TEMP, userId, filePart.filename() );

        filePart.transferTo(Paths.get(fileName).toFile()).doOnSuccess(t -> {
            String msg = String.format(MessageConstants.MSG_FILE_SAVED_SUCCESSFUL, filePart.filename());
            log.info(MessageConstants.prefixMsg(msg));
            sink.success(new FileStatus(true, fileName, "", resourceUri));
        }).doOnError(err -> {
            String msg = String.format(MessageConstants.MSG_ERR_FILE_SAVING, filePart.filename(), err.getMessage());
            log.info(MessageConstants.prefixMsg(msg));
            sink.success(new FileStatus(false, fileName, msg));
        }).subscribe();
    }
}
