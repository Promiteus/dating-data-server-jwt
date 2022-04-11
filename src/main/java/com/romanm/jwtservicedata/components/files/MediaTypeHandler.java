package com.romanm.jwtservicedata.components.files;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MediaTypeHandler {
    private final Map<String, String> imageFormatsMediaTypesMap = Map.of(
            "png", MediaType.IMAGE_PNG_VALUE,
            "jpg", MediaType.IMAGE_JPEG_VALUE,
            "jpeg", MediaType.IMAGE_JPEG_VALUE,
            "gif", MediaType.IMAGE_GIF_VALUE
    );

    /**
     * Получить медиатип по имени файла
     * @param fileName String
     * @return String
     */
    public final String getFileMediaType(String fileName) {
        if ((fileName == null) || (fileName.isEmpty())) {
           return null;
        }

        String dotRegexp = "\\.";
        String fileExtension = fileName.split(dotRegexp)[fileName.split(dotRegexp).length-1];
        return imageFormatsMediaTypesMap.get(fileExtension);
    }
}
