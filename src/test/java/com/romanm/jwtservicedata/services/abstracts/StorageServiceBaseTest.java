package com.romanm.jwtservicedata.services.abstracts;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

public class StorageServiceBaseTest  {
    //private StorageServiceBase storageServiceBase = new StorageServiceBase("uploads_test");

    @Test
    public void saveFileTest() {
       /* MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("partFile", new ClassPathResource("/test/file1.png"));

        MultiValueMap<String, HttpEntity<?>> map = multipartBodyBuilder.build();



        this.storageServiceBase.save(Mono.empty(), "200", 3);*/
    }
}