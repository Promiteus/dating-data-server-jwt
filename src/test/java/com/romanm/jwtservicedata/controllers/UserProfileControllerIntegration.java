package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.configs.auth.TestSecurityConfiguration;
import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.builders.UserProfileBuilder;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import com.romanm.jwtservicedata.services.interfaces.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Calendar;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = {"test"})
@Import(value = TestSecurityConfiguration.class)
public class UserProfileControllerIntegration {
    @Autowired
    private UserProfileService userProfileService;

   // private static final String BREARER_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyb20zODg5QHlhbmRleC5ydSIsImV4cCI6MTYzNzczNjA3Mn0.H4LXCNO7NKUMrTu7DrTuYFwpk7K1MqVsoAxB74K6dMIg_bJgnIFe0YVPHjR8IjVgkwZSuKYuW0ITAhQsfW9_PQ";

    @Autowired
    private WebTestClient webTestClient;

    private UserProfile getUserProfile() {
        Calendar c = Calendar.getInstance();
        c.set(1987, Calendar.MAY, 23, 0, 0);
        return UserProfileBuilder.create("100")
          .setFirstName("Roman")
          .setLastName("Matveev")
          .setBirthDate(c.getTime())
          .setKids(0)
          .setWeight(68)
          .setHeight(170)
          .setAboutMe("Обо мне ")
          .setFamilyStatus(CommonConstants.FamilyStatus.MARRIED)
          .setSex(CommonConstants.Sex.MAN)
          .setSexOrientation(CommonConstants.SexOrientation.HOMO)
          .setRank(2000).build();
    }

    private WebClient createWebClient() {
        return WebClient.builder()
                .baseUrl(Api.BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    private void saveOrUpdateUserProfileRequestPostTest() {
        UserProfile userProfile = this.getUserProfile();
        userProfile.setId("100001");
        this.userProfileService.removeUserProfile(userProfile.getId(), false).block();

        log.info(MessageConstants.prefixMsg("Trying to save new profile..."));
        this.webTestClient.post()
                .uri(Api.API_PREFIX+Api.API_USER_PROFILE)
                .body(Mono.just(userProfile), UserProfile.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectBody(UserProfile.class)
                .value(res -> {
                    log.info(MessageConstants.prefixMsg("Saved profile with id: "+userProfile.getId()));
                });

        log.info(MessageConstants.prefixMsg("Trying to update existing profile..."));
        userProfile.setFirstName("Sergey");
        userProfile.setLastName("Chertinov");
        this.webTestClient.post()
                .uri(Api.API_PREFIX+Api.API_USER_PROFILE)
                .body(Mono.just(userProfile), UserProfile.class)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isAccepted()
                .expectBody(UserProfile.class)
                .value(res -> {
                    log.info(MessageConstants.prefixMsg("Updated profile with id: "+userProfile.getId()));
                });

        //Удалить тестируемую запись
        this.userProfileService.removeUserProfile(userProfile.getId(), false).block();
    }

    private void getResponseUserProfileGetTest() {
        UserProfile userProfile = this.getUserProfile();
        userProfile.setId("100002");

        this.userProfileService.removeUserProfile(userProfile.getId(), false).block();
        this.userProfileService.saveOrUpdateUserProfile(userProfile).block();

        //Получить тестовую запись
        this.webTestClient.get()
                .uri(Api.API_PREFIX+Api.API_USER_PROFILE+"/"+userProfile.getId())
                //.header(MessageConstants.HEADER_STRING, MessageConstants.TOKEN_PREFIX + BREARER_KEY)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResponseUserProfile.class)
                .value(resp -> {
                    log.info(MessageConstants.prefixMsg("Got response data: "+resp));
                    this.userProfileService.removeUserProfile(userProfile.getId(), false).block();
                });
    }

    private void removeUserProfileAndCheckTest() throws InterruptedException {
        UserProfile userProfile = this.getUserProfile();
        userProfile.setId("2839940555");
        //Создать тестовую запись
        this.userProfileService.saveOrUpdateUserProfile(userProfile).block();

        log.info(MessageConstants.prefixMsg("Trying to delete created profile..."));
        //Удалить тестовую запись
        this.webTestClient.delete()
                .uri(Api.API_PREFIX+Api.API_USER_PROFILE+"/"+userProfile.getId())
                .exchange()
                .expectStatus()
                .isAccepted();

        Thread.sleep(1000);

        log.info(MessageConstants.prefixMsg("Trying find removed profile..."));
        this.webTestClient.get()
                .uri(Api.API_PREFIX+Api.API_USER_PROFILE+"/"+userProfile.getId())
                .exchange()
                .expectStatus()
                .isNotFound()
                .expectBody(ResponseUserProfile.class)
                .value(resp -> {
                    log.info(MessageConstants.prefixMsg("Found profile: "+resp));
                    if (resp == null) {
                        log.info(MessageConstants.prefixMsg(String.format("Profile '%s' was deleted successfully!", userProfile.getId())));
                    }
                });
    }

    @Test
    public void stepRestTest() throws InterruptedException {
        //Получить профиль пользователя из конечной точки
        this.getResponseUserProfileGetTest();
        //Сохранить профиль пользователя из конечной точки
        this.saveOrUpdateUserProfileRequestPostTest();
        //Удалить профиль пользователя из конечной точки
        this.removeUserProfileAndCheckTest();
    }
}
