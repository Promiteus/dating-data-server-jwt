package com.romanm.jwtservicedata.configs.mongodb;

import com.mongodb.reactivestreams.client.MongoClient;
import com.romanm.jwtservicedata.constants.CommonConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

@Configuration
public class ReactiveMongoConfig {
    @Autowired
    MongoClient mongoClient;

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(mongoClient, CommonConstants.MONGO_DB);
    }
}
