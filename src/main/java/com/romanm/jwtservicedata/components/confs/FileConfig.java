package com.romanm.jwtservicedata.components.confs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "files")
public class FileConfig {
    private int maxCount;
    private String uploadsDir;
    private List<String> permittedFormats;
}
