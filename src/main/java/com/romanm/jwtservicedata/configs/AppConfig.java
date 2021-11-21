package com.romanm.jwtservicedata.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanm.jwtservicedata.components.preload.DataPreloader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableReactiveMongoRepositories(value = "com.romanm.jwtservicedata.repositories")
public class AppConfig implements CommandLineRunner {
    @Autowired
    private DataPreloader dataPreloader;

    @Bean
    public BCryptPasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void run(String... args) throws Exception {
         //Заполнить начальными профилями базу
         this.dataPreloader.fillStarterData();
    }
}
