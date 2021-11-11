package com.romanm.jwtservicedata.services.profile;

import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.repositories.UserProfileRepository;
import com.romanm.jwtservicedata.services.interfaces.IUserProfileService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.util.Calendar;
import java.util.Date;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserProfileServiceV1Test {

    private static final String TEST_USER_ID = "12";

    @Autowired
    private IUserProfileService userProfileService;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Test
    public void getUserProfileMonoBlockTest() {
        Mono<UserProfile> userProfileMono = this.userProfileRepository.findUserProfileByUserId("12");

        UserProfile userProfile = userProfileMono.block();

        log.info(MessageConstants.prefixMsg("userProfile instant: "+userProfile));
    }

    @Test
    public void getUserProfileMonoTest() {
       // this.userProfileRepository.deleteByUserId(TEST_USER_ID).block();


        UserProfile userProfileTest = new UserProfile();
        userProfileTest.setUserId(TEST_USER_ID);
        userProfileTest.setFirstName("Roman");
        userProfileTest.setLastName("Matveev");
        userProfileTest.setBirthDate(new Date(1987, Calendar.MAY, 22));
        userProfileTest.setKids(0);
        userProfileTest.setWeight(68);
        userProfileTest.setHeight(170);
        userProfileTest.setAboutMe("Обо мне");
        this.userProfileRepository.save(userProfileTest).subscribe(profile -> {
            log.info(MessageConstants.prefixMsg("userProfile save: " + profile));
        });

        Mono<UserProfile> userProfileMono = this.userProfileRepository.findUserProfileByUserId("12");

        userProfileMono.doOnSuccess(userProfile -> {
            log.info(MessageConstants.prefixMsg("userProfile doOnSuccess: " + userProfile));
        }).doOnSubscribe(cons -> {
            log.info(MessageConstants.prefixMsg("userProfile doOnSubscribe: " + cons));
        }).doOnError(error -> {
            log.info(MessageConstants.prefixMsg("Error: " + error.getMessage()));
        }).subscribe(userProfile -> {
            log.info(MessageConstants.prefixMsg("userProfile instant: " + userProfile));

            this.userProfileRepository.delete(userProfile).block();
            log.info(MessageConstants.prefixMsg("Removed profile with userId: " + userProfileTest.getUserId()));
        });


    }
}