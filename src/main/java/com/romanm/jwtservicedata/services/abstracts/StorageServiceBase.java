package com.romanm.jwtservicedata.services.abstracts;

import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.constants.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.FileSystemUtils;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class StorageServiceBase {
    private final Path fileDirectory;

    public StorageServiceBase(String dir) {
        this.fileDirectory = Paths.get(dir);
    }

    protected void createWorkDir(String dir) {
        try {
             Files.createDirectory(Paths.get(dir));
        } catch (IOException e) {
            log.error(MessageConstants.errorPrefixMsg(e.getMessage()));
        }
    }

    protected boolean dirExists(String dir) {
        return Files.exists(Paths.get(dir));
    }

    private Mono<Boolean> saveFileItem(FilePart filePart) {
        return Mono.create(monoSink -> {
            filePart.transferTo(this.fileDirectory).doOnSuccess(t -> {
                log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_FILE_SAVED_SUCCESSFUL, filePart.filename())));
                monoSink.success(true);
            }).doOnError(err -> {
                monoSink.success(false);
            }).subscribe();
        });
    }

    protected Mono<Boolean> save(Mono<FilePart> file) {
        if (!this.dirExists(CommonConstants.MULTIMEDIA_FILE_DIR)) {
            this.createWorkDir(CommonConstants.MULTIMEDIA_FILE_DIR);
        }

        return Mono.create(sink -> {
            file.doOnSuccess(filePart -> {
                filePart.transferTo(this.fileDirectory).doOnSuccess(t -> {
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

    protected void deleteFile(String fileName) {
       // Files.delete();
    }

    protected void deleteAll() {
        FileSystemUtils.deleteRecursively(fileDirectory.toFile());
    }
}
