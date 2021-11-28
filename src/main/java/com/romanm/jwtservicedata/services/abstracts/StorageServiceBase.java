package com.romanm.jwtservicedata.services.abstracts;

import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.constants.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
public class StorageServiceBase {
    private final String baseDir;

    public StorageServiceBase(String baseDir) {
        this.baseDir = baseDir;
    }

    protected void createWorkDir(String dir) {
        try {
             Files.createDirectory(Paths.get(dir));
        } catch (IOException e) {
            log.error(MessageConstants.errorPrefixMsg(e.getMessage()));
        }
    }

    protected boolean isDirExists(String dir) {
        return Files.exists(Paths.get(dir));
    }

    private Mono<Boolean> saveFileItem(FilePart filePart, String userId) {
        return Mono.create(monoSink -> {
            String fileName = String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.baseDir, userId)+"/"+filePart.filename();

            filePart.transferTo(Paths.get(fileName)).doOnSuccess(t -> {
                log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_FILE_SAVED_SUCCESSFUL, filePart.filename())));
                monoSink.success(true);
            }).doOnError(err -> {
                log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_ERR_FILE_SAVING, filePart.filename(), err.getMessage())));
                monoSink.success(false);
            }).subscribe();
        });
    }

    /**
     *
     * @param file Mono<FilePart>
     * @param userId String
     * @return Mono<Boolean>
     */
    protected Mono<Boolean> save(Mono<FilePart> file, String userId) {
        if (!this.isDirExists(this.baseDir)) {
            this.createWorkDir(this.baseDir);
        }

        if (!this.isDirExists(String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.baseDir, userId))) {
            this.createWorkDir(String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.baseDir, userId));
        }

        return Mono.create(sink -> {
            file.doOnSuccess(filePart -> {
                String fileName = String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.baseDir, userId)+"/"+filePart.filename();

                filePart.transferTo(Paths.get(fileName).toFile()).doOnSuccess(t -> {
                    log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_FILE_SAVED_SUCCESSFUL, filePart.filename())));
                    sink.success(true);
                }).doOnError(err -> {
                    log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_ERR_FILE_SAVING, filePart.filename(), err.getMessage())));
                    sink.success(false);
                }).subscribe();

            }).doOnError(err -> {
                log.info(MessageConstants.errorPrefixMsg(err.getMessage()));
                sink.success(false);
            }).subscribe();
        });
    }

    protected void deleteUserFile(String fileName, String userId) {
        try {
            Files.deleteIfExists(Paths.get(String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.baseDir, userId)+"/"+fileName));
        } catch (IOException e) {
            log.error(MessageConstants.errorPrefixMsg(e.getMessage()));
        }
    }

    protected void deleteAllUserFiles(String fileName, String userId) {
        FileSystemUtils.deleteRecursively(Paths.get(String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.baseDir, userId)).toFile());
    }


    protected void deleteAll() {
        FileSystemUtils.deleteRecursively(Paths.get(this.baseDir).toFile());
    }
}
