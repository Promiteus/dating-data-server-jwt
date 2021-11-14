package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import com.romanm.jwtservicedata.services.interfaces.IUserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Calendar;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserProfileControllerIntegration {
    @Autowired
    private IUserProfileService userProfileService;

    private static final String BREARER_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb20zODg5QHlhbmRleC5ydSIsImV4cCI6MTYzNzczNjA3Mn0.H4LXCNO7NKUMrTu7DrTuYFwpk7K1MqVsoAxB74K6dMIg_bJgnIFe0YVPHjR8IjVgkwZSuKYuW0ITAhQsfW9_PQ";

    @Autowired
    private WebTestClient webTestClient;

    private UserProfile getUserProfile() {
        UserProfile userProfileTest = new UserProfile();
        userProfileTest.setId("100");
        userProfileTest.setFirstName("Roman");
        userProfileTest.setLastName("Matveev");
        Calendar c = Calendar.getInstance();
        c.set(1987, Calendar.MAY, 23, 0, 0);
        userProfileTest.setBirthDate(c.getTime());
        userProfileTest.setKids(0);
        userProfileTest.setWeight(68);
        userProfileTest.setHeight(170);
        userProfileTest.setAboutMe("Обо мне ");
        userProfileTest.setFamilyStatus(CommonConstants.FamilyStatus.MARRIED);
        userProfileTest.setSex(CommonConstants.Sex.MAN);
        userProfileTest.setSexOrientation(CommonConstants.SexOrientation.HOMO);
        userProfileTest.setRank(2000);

        return userProfileTest;
    }

    private WebClient createWebClient() {
        return WebClient.builder()
                .baseUrl(Api.BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private void saveOrUpdateUserProfileRequestPostTest(WebClient webClient) {
        UserProfile userProfile1 = this.getUserProfile();
        userProfile1.setId("100001");

        webClient.post()
                .uri(Api.API_PREFIX+Api.API_USER_PROFILE)
                .body(Mono.just(userProfile1), UserProfile.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(UserProfile.class)
                .doOnSuccess(res -> {
                    log.info(MessageConstants.prefixMsg("Saved profile with id: "+userProfile1.getId()));
                }).doOnError(error -> {
                    log.info(MessageConstants.prefixMsg(String.format("Error was occured for 'id=%s'. Error: %s", userProfile1.getId(),  error.getMessage())));
                }).subscribe();
    }

    private void getResponseUserProfileGetTest(WebClient webClient) {
        UserProfile userProfile = this.getUserProfile();
        userProfile.setId("100002");

        this.userProfileService.removeUserProfile(userProfile.getId(), false).block();
        this.userProfileService.saveOrUpdateUserProfile(userProfile).block();


        this.webTestClient.get()
                .uri(Api.API_PREFIX+Api.API_USER_PROFILE+"/100002")
                .header(MessageConstants.HEADER_STRING, MessageConstants.TOKEN_PREFIX + BREARER_KEY)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResponseUserProfile.class)
                .value(resp -> {
                    log.info(MessageConstants.prefixMsg("Got response data: "+resp));
                });
    }

    @Test
    public void stepRestTest()  {
        WebClient webClient = this.createWebClient();
        //this.saveOrUpdateUserProfileRequestPostTest(webClient);
        this.getResponseUserProfileGetTest(webClient);

    }
}
