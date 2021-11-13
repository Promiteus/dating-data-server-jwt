package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import com.romanm.jwtservicedata.services.interfaces.IUserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Calendar;

@Slf4j
@RunWith(SpringRunner.class)
@WebFluxTest(controllers = UserProfileController.class,  excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
public class UserProfileControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private IUserProfileService userProfileService;

    public void initEmptyVisitors() {
        log.info(MessageConstants.prefixMsg("initEmptyVisitors() test"));

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

        //200
        Mockito.when(this.userProfileService.getUserProfile("100")).thenReturn(Mono.just(new ResponseUserProfile(userProfileTest, new ArrayList<>())));
        //NOT FOUND 404
        Mockito.when(this.userProfileService.getUserProfile("20")).thenReturn(Mono.just(new ResponseUserProfile(null, new ArrayList<>())));

        //ResponseUserProfile responseUserProfile = this.userProfileService.getUserProfile("100").block();
        log.info(MessageConstants.prefixMsg(this.userProfileService.getUserProfile("100").block().toString()));
        log.info(MessageConstants.prefixMsg(this.userProfileService.getUserProfile("20").block().toString()));
    }

    /**
     * Тест запроса GET /api/user_profile/100 (200)
     */
    @Test
    public void getUserProfileEmptyVisitorsV1() {
        log.info(MessageConstants.prefixMsg("getUserProfileEmptyVisitorsV1() test"));
        this.initEmptyVisitors();

        this.webTestClient
                .get()
                .uri(Api.API_PREFIX+Api.API_USER_PROFILE+"/100")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResponseUserProfile.class)
                .value(body -> {
                    log.info(MessageConstants.prefixMsg("Got body: "+body));
                    Assert.assertEquals("100", body.getUserProfile().getId());
                });
    }

    /**
     * Тест запроса GET /api/user_profile (405)
     */
    @Test
    public void getUserProfileEmptyVisitorsV2() {
        log.info(MessageConstants.prefixMsg("getUserProfileEmptyVisitorsV2() test"));
        this.initEmptyVisitors();

        this.webTestClient
                .get()
                .uri(Api.API_PREFIX+Api.API_USER_PROFILE)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    /**
     * Тест запроса GET /api/user_profile/20 (404)
     */
    @Test
    public void getUserProfileEmptyVisitorsV3() {
        log.info(MessageConstants.prefixMsg("getUserProfileEmptyVisitorsV1() test"));
        this.initEmptyVisitors();

        this.webTestClient
                .get()
                .uri(Api.API_PREFIX+Api.API_USER_PROFILE+"/20")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void updateOrSaveUserProfile() {
        log.info(MessageConstants.prefixMsg("updateOrSaveUserProfile() test"));


    }

    @Test
    public void removeUserProfile() {
        log.info(MessageConstants.prefixMsg("updateOrSaveUserProfile() test"));
        this.initEmptyVisitors();

        this.webTestClient
                .delete()
                .uri(Api.API_PREFIX+Api.API_USER_PROFILE+"/100")
                .exchange()
                .expectStatus()
                .isOk();
    }
}