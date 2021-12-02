package com.romanm.jwtservicedata.components.confs;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "dating")
@Data
public class FileConfig {
    private int filesMaxCount;
}
