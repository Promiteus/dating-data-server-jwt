package com.romanm.jwtservicedata.components.confs;

import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.responses.files.FileStatus;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.MonoSink;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "files")
public class FileConfig {
    private int maxCount;
    private String uploadsDir;
    private List<String> permittedFormats;
    private String thumbDir;
    private int thumbWidth;
    private String thumbExt;

    /**
     * Проверить, является ли файл разрешенным
     * @param fileName String
     * @return FileStatus
     */
    private FileStatus isPermittedFileType(String fileName) {
        FileStatus fileStatusDefault = new FileStatus(false, fileName, String.format(MessageConstants.MSG_FORMAT_FILE_INVALID, fileName, this.getPermittedFormats()));

        if ((fileName == null) || (fileName.isEmpty())) {
            return fileStatusDefault;
        }

        String dotRegexp = "\\.";
        String fileExtension = fileName.split(dotRegexp)[fileName.split(dotRegexp).length-1];

        if (this.getPermittedFormats().contains(fileExtension)) {
            return new FileStatus(true, fileName, "");
        }

        return fileStatusDefault;
    }

    /**
     *
     * @param sink MonoSink<FileStatus>
     * @param filePart FilePart
     * @return boolean
     */
    public boolean isFileFormatConfirmed(MonoSink<FileStatus> sink, FilePart filePart) {
        FileStatus fileStatusRes = this.isPermittedFileType(filePart.filename());
        if (!fileStatusRes.isSaved()) {
            sink.success(fileStatusRes);
            return false;
        }
        return true;
    }

    /**
     * Получить файл из ресурса и проверить на исключение
     * @param resource Resource
     * @return File
     */
    private File resourceToFile(Resource resource) {
        try {
            return resource.getFile();
        } catch (IOException e) {
            log.error(MessageConstants.errorPrefixMsg(e.getMessage()));
        }
        return null;
    }

    /**
     * Получить список тестовых файлов
     * @return List<File>
     */
    public List<File> resourceTestFileList(Resource[] resFilesTest) {
        return Arrays.stream(resFilesTest).map(this::resourceToFile).filter(Objects::nonNull).filter(File::isFile).collect(Collectors.toList());
    }

    /**
     * Выдаст список файлов в каталоге пользователя
     * @param userId String
     * @return List<File>
     */
    public List<File> listFiles(String userId) {
        try (Stream<Path> files = Files.list(Paths.get(String.format(CommonConstants.MULTIMEDIA_DEST_DIR, this.getUploadsDir(), userId)))) {
            return files.map(Path::toFile).filter(File::isFile).collect(Collectors.toList());
        } catch (IOException e) {
            log.error(MessageConstants.errorPrefixMsg(e.getMessage()));
        }
        return List.of();
    }

    /**
     * Найти нужный файл по имени из списка файлов
     * @param fileName String
     * @param userId String
     * @return File
     */
    public File findFile(String fileName, String userId) {
        return this.listFiles(userId).stream().filter(f -> (f.getName().equals(fileName))).findFirst().get();
    }
}
