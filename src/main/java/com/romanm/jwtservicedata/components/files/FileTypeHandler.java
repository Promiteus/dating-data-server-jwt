package com.romanm.jwtservicedata.components.files;

import com.romanm.jwtservicedata.components.confs.FileConfig;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.responses.files.FileStatus;

public class FileTypeHandler {
    private final FileConfig fileConfig;

    /**
     * Конструктор класса FileTypeHandler
     * @param fileConfig FileConfig
     */
    public FileTypeHandler(FileConfig fileConfig) {
        this.fileConfig = fileConfig;
    }

    /**
     * Проверить, является ли файл разрешенным
     * @param fileName String
     * @return FileStatus
     */
    public final FileStatus isPermittedFileType(String fileName) {
        FileStatus fileStatusDefault = new FileStatus(false, fileName, String.format(MessageConstants.MSG_FORMAT_FILE_INVALID, fileName, fileConfig.getPermittedFormats()));

        if ((fileName == null) || (fileName.isEmpty())) {
            return fileStatusDefault;
        }

        String dotRegexp = "\\.";
        String fileExtension = fileName.split(dotRegexp)[fileName.split(dotRegexp).length-1];

        if (this.fileConfig.getPermittedFormats().contains(fileExtension)) {
            return new FileStatus(true, fileName, "");
        }

        return fileStatusDefault;
    }
}
