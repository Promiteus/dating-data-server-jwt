package com.romanm.jwtservicedata.components.http;

import com.romanm.jwtservicedata.constants.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@Slf4j
@RunWith(SpringRunner.class)
public class MediaTypeConvertorTest {
    public static final String FILE_MEDIA_ASSOC = "File %s -> media type %s";

    @Test
    public void getFileMediaTypeTest() {
         List<String> files = List.of(
                 "4556.png",
                 "4555.jpeg",
                 "4667.jpg",
                 "5556.gif",
                 "5k666.bmp",
                 "4kekee.so",
                 "4jj5.sssf.jpg"
         );

         MediaTypeConvertor mediaTypeConvertor = new MediaTypeConvertor();
         files.forEach(fileItem -> {
             String mediaType = mediaTypeConvertor.getFileMediaType(fileItem);
             log.info(MessageConstants.prefixMsg(String.format(FILE_MEDIA_ASSOC, fileItem, mediaType)));
         });

        String fileName1 = null;
        String mediaType1 = mediaTypeConvertor.getFileMediaType(fileName1);
        log.info(MessageConstants.prefixMsg(String.format(FILE_MEDIA_ASSOC, fileName1, mediaType1)));
        Assert.assertNull(mediaType1);

        String fileName2 = "";
        String mediaType2 = mediaTypeConvertor.getFileMediaType(fileName2);
        log.info(MessageConstants.prefixMsg(String.format(FILE_MEDIA_ASSOC, fileName2, mediaType2)));
        Assert.assertNull(mediaType2);

        String fileName3 = "test";
        String mediaType3 = mediaTypeConvertor.getFileMediaType(fileName3);
        log.info(MessageConstants.prefixMsg(String.format(FILE_MEDIA_ASSOC, fileName3, mediaType3)));
        Assert.assertNull(mediaType3);

        String fileName4 = "test.";
        String mediaType4 = mediaTypeConvertor.getFileMediaType(fileName4);
        log.info(MessageConstants.prefixMsg(String.format(FILE_MEDIA_ASSOC, fileName4, mediaType4)));
        Assert.assertNull(mediaType4);

        String fileName5 = "test.so";
        String mediaType5 = mediaTypeConvertor.getFileMediaType(fileName5);
        log.info(MessageConstants.prefixMsg(String.format(FILE_MEDIA_ASSOC, fileName5, mediaType5)));
        Assert.assertNull(mediaType5);
    }
}