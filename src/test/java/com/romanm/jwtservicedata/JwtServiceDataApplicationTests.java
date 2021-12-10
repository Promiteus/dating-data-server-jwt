package com.romanm.jwtservicedata;

import com.romanm.jwtservicedata.constants.Api;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
class JwtServiceDataApplicationTests {

    @Test
    void contextLoads() {
    }


   /* @Bean
    public WebClient client() {
        return WebClient.create(Api.BASE_URL);
    }*/
}
