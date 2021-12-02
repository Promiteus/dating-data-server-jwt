package com.romanm.jwtservicedata.configs;

import com.romanm.jwtservicedata.components.confs.FileConfig;
import com.romanm.jwtservicedata.components.preload.DataPreloader;
import com.romanm.jwtservicedata.constants.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
@Configuration
@PropertySource("classpath:/config/filescfg.properties")
@EnableReactiveMongoRepositories(value = "com.romanm.jwtservicedata.repositories")
public class AppConfig implements CommandLineRunner {
    @Autowired
    private DataPreloader dataPreloader;
    @Autowired
    private FileConfig fileConfig;

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
         //Заполнить начальными профилями базу
         this.dataPreloader.fillStarterData();

         log.info(MessageConstants.prefixMsg("files count: "+fileConfig));
    }
}
