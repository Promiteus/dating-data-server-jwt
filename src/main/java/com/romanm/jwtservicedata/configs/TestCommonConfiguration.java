package com.romanm.jwtservicedata.configs;

import com.romanm.jwtservicedata.components.confs.FileConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Profile(value = "test")
@TestConfiguration
public class TestCommonConfiguration {

  /*  @Bean
    FileConfig fileConfig() {
        FileConfig fileConfig = new FileConfig();
        fileConfig.setUploadsDir("uploads_test");
        fileConfig.setMaxCount(3);
        fileConfig.setPermittedFormats(List.of("jpg","jpeg","png"));
        return fileConfig;
    }*/
}
