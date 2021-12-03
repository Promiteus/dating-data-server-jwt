package com.romanm.jwtservicedata.components.confs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "files")
public class FileConfig {
    private int maxCount;
    private String uploadsDir;
    private String permittedFormats;

    /**
     * Допустимые форматы файлов
     * @return  String[]
     */
    public String[] getPermittedFormats() {
        return permittedFormats.split("#");
    }
}
