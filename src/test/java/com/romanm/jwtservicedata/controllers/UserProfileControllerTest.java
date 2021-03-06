package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.configs.auth.TestSecurityConfiguration;
import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.responses.profile.ResponseUserProfile;
import com.romanm.jwtservicedata.services.interfaces.UserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Calendar;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = {"test"})
@Import(value = {TestSecurityConfiguration.class})
public class UserProfileControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    @Qualifier("userProfileServiceV1")
    private UserProfileService userProfileService;

    @Autowired
    private ApplicationContext context;

   /* @Configuration
    static class ServiceTestConf {
        @Bean
        public IUserProfileService getUserProfileService() {
            return new UserProfileServiceV1();
        }
    }*/

    @Before
    public void setUp()
    {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

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
        userProfileTest.setAboutMe("?????? ?????? ");
        userProfileTest.setFamilyStatus(CommonConstants.FamilyStatus.MARRIED);
        userProfileTest.setSex(CommonConstants.Sex.MAN);
        userProfileTest.setSexOrientation(CommonConstants.SexOrientation.HOMO);
        userProfileTest.setRank(2000);

        return userProfileTest;
    }

    public void initEmptyVisitors() {
        log.info(MessageConstants.prefixMsg("initEmptyVisitors() test"));

        UserProfile userProfileTest = this.getUserProfile();
        //200
        Mockito.when(this.userProfileService.getUserProfile("100")).thenReturn(Mono.just(new ResponseUserProfile(userProfileTest, new ArrayList<>(), new ArrayList<>())));
        //NOT FOUND 404
        Mockito.when(this.userProfileService.getUserProfile("20")).thenReturn(Mono.just(new ResponseUserProfile(null, new ArrayList<>(), new ArrayList<>())));

        //ResponseUserProfile responseUserProfile = this.userProfileService.getUserProfile("100").block();
        log.info(MessageConstants.prefixMsg(this.userProfileService.getUserProfile("100").block().toString()));
        log.info(MessageConstants.prefixMsg(this.userProfileService.getUserProfile("20").block().toString()));
    }

    /**
     * ???????? ?????????????? GET /api/user_profile/100 (200)
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
     * ???????? ?????????????? GET /api/user_profile (405)
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
     * ???????? ?????????????? GET /api/user_profile/20 (404)
     */
    @Test
    public void getUserProfileEmptyVisitorsV3() {
        log.info(MessageConstants.prefixMsg("getUserProfileEmptyVisitorsV3() test"));
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
        UserProfile userProfile = this.getUserProfile();
        userProfile.setId("10001");

        Mockito.when(this.userProfileService.saveOrUpdateUserProfile(userProfile)).thenReturn(Mono.just(userProfile));

      /*  this.webTestClient
                .post()
                .uri(Api.API_PREFIX+Api.API_USER_PROFILE)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(userProfile), UserProfile.class)
                .exchange()
                .expectStatus()
                .isAccepted();*/

      //  Mockito.verify(this.userProfileService, Mockito.times(1)).saveOrUpdateUserProfile(userProfile);
    }

    @Test
    public void removeUserProfile202() {
        log.info(MessageConstants.prefixMsg("updateOrSaveUserProfile202() test"));
        Mockito.when(this.userProfileService.removeUserProfile("100", false)).thenReturn(Mono.just(true));

        this.webTestClient
                .delete()
                .uri(Api.API_PREFIX+Api.API_USER_PROFILE+"/100")
                .exchange()
                .expectStatus()
                .isAccepted();

        log.info(MessageConstants.prefixMsg("Deleting was successful, code 202!"));
    }

    @Test
    public void removeUserProfile304() {
        log.info(MessageConstants.prefixMsg("updateOrSaveUserProfile304() test"));
        Mockito.when(this.userProfileService.removeUserProfile("20", false)).thenReturn(Mono.just(false));

        this.webTestClient
                .delete()
                .uri(Api.API_PREFIX+Api.API_USER_PROFILE+"/20")
                .exchange()
                .expectStatus()
                .isNotModified();

        log.info(MessageConstants.prefixMsg("Deleting was failed, code 304!"));
    }
}