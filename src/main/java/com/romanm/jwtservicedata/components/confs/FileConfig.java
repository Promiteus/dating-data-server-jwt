package com.romanm.jwtservicedata.components.confs;

import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.responses.files.FileStatus;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.MonoSink;

import java.util.List;

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
}
