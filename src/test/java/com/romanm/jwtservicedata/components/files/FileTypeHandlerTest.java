package com.romanm.jwtservicedata.components.files;

import com.romanm.jwtservicedata.components.confs.FileConfig;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.responses.files.FileStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
public class FileTypeHandlerTest {
    private static final String FILE_STATUS_FOR_FILE = "File status for '%s' is %s";

    @Test
    public void permittedTypesTest() {
        FileConfig fileConfig = new FileConfig();
        fileConfig.setPermittedFormats(List.of("jpg", "jpeg", "png"));
        FileTypeHandler fileTypeHandler = new FileTypeHandler(fileConfig);

        FileStatus fileStatus;
        String filePng = "file.png";
        fileStatus = fileTypeHandler.isPermittedFileType(filePng);
        log.info(MessageConstants.prefixMsg(String.format(FILE_STATUS_FOR_FILE, filePng, fileStatus.isSaved())));
        Assert.assertTrue(fileStatus.isSaved());

        String fileJpg = "file.jpg";
        fileStatus = fileTypeHandler.isPermittedFileType(fileJpg);
        log.info(MessageConstants.prefixMsg(String.format(FILE_STATUS_FOR_FILE, fileJpg, fileStatus.isSaved())));
        Assert.assertTrue(fileStatus.isSaved());

        String fileSo = "file.so";
        fileStatus = fileTypeHandler.isPermittedFileType(fileSo);
        log.info(MessageConstants.prefixMsg(String.format(FILE_STATUS_FOR_FILE, fileSo, fileStatus.isSaved())));
        Assert.assertFalse(fileStatus.isSaved());

        String fileNoExt = "file";
        fileStatus = fileTypeHandler.isPermittedFileType(fileNoExt);
        log.info(MessageConstants.prefixMsg(String.format(FILE_STATUS_FOR_FILE, fileNoExt, fileStatus.isSaved())));
        Assert.assertFalse(fileStatus.isSaved());

        String fileEmpty = "";
        fileStatus = fileTypeHandler.isPermittedFileType(fileEmpty);
        log.info(MessageConstants.prefixMsg(String.format(FILE_STATUS_FOR_FILE, fileEmpty, fileStatus.isSaved())));
        Assert.assertFalse(fileStatus.isSaved());

        String fileNull = null;
        fileStatus = fileTypeHandler.isPermittedFileType(fileNull);
        log.info(MessageConstants.prefixMsg(String.format(FILE_STATUS_FOR_FILE, fileNull, fileStatus.isSaved())));
        Assert.assertFalse(fileStatus.isSaved());
    }
}