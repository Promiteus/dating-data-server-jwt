package com.romanm.jwtservicedata.components.preload;

import com.romanm.jwtservicedata.components.preload.interfaces.SingleSaver;
import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.builders.UserProfileBuilder;
import com.romanm.jwtservicedata.repositories.ChatMessageRepository;
import com.romanm.jwtservicedata.repositories.UserProfileRepository;
import com.romanm.jwtservicedata.repositories.VisitorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Slf4j
@Component
public class DataPreloader {

    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private VisitorRepository visitorRepository;

    /**
     * Заполнить коллекцию тестовыми профилями
     * @return Flux<UserProfile>
     */
    private Flux<UserProfile> fillUserProfileCollectionByStartData() {
        Calendar c = Calendar.getInstance();

            List<UserProfile> userProfiles = new ArrayList<>();

            c.set(1987, Calendar.MAY, 23, 0, 0);
            UserProfile roman = UserProfileBuilder.create("200")
                    .setFirstName("Roman")
                    .setLastName("Matveev")
                    .setBirthDate(c.getTime())
                    .setKids(0)
                    .setWeight(68)
                    .setHeight(170)
                    .setAboutMe("О Романе Матвееве")
                    .setFamilyStatus(CommonConstants.FamilyStatus.SINGLE)
                    .setSex(CommonConstants.Sex.MAN)
                    .setSexOrientation(CommonConstants.SexOrientation.HETERO)
                    .setRank(2000).build();
            userProfiles.add(roman);

            c.set(1990, Calendar.FEBRUARY, 2, 0, 0);
            UserProfile egor = UserProfileBuilder.create("201")
                    .setFirstName("Egor")
                    .setLastName("Privalov")
                    .setBirthDate(c.getTime())
                    .setKids(2)
                    .setWeight(62)
                    .setHeight(178)
                    .setAboutMe("О Егоре Привалове")
                    .setFamilyStatus(CommonConstants.FamilyStatus.MARRIED)
                    .setSex(CommonConstants.Sex.MAN)
                    .setSexOrientation(CommonConstants.SexOrientation.HETERO)
                    .setRank(2000).build();
            userProfiles.add(egor);

            c.set(1992, Calendar.MAY, 6, 0, 0);
            UserProfile artem = UserProfileBuilder.create("202")
                    .setFirstName("Artem")
                    .setLastName("Mhitorian")
                    .setBirthDate(c.getTime())
                    .setKids(0)
                    .setWeight(62)
                    .setHeight(178)
                    .setAboutMe("О Артеме Мхиторяне")
                    .setFamilyStatus(CommonConstants.FamilyStatus.MARRIED)
                    .setSex(CommonConstants.Sex.MAN)
                    .setSexOrientation(CommonConstants.SexOrientation.HETERO)
                    .setRank(2000).build();
            userProfiles.add(artem);

            c.set(1987, Calendar.MAY, 22, 0, 0);
            UserProfile kot = UserProfileBuilder.create("203")
                    .setFirstName("Konstantin")
                    .setLastName("Matveev")
                    .setBirthDate(c.getTime())
                    .setKids(0)
                    .setWeight(64)
                    .setHeight(175)
                    .setAboutMe("О Константине Матвееве")
                    .setFamilyStatus(CommonConstants.FamilyStatus.MARRIED)
                    .setSex(CommonConstants.Sex.MAN)
                    .setSexOrientation(CommonConstants.SexOrientation.HETERO)
                    .setRank(2000).build();
            userProfiles.add(kot);

        return this.saveUserProfiles(userProfiles);
    }

    public void fillStarterData() {
        if (this.userProfileRepository.count().block() == 0) {
            //Заполнить коллекцию начальными профилями
            this.fillUserProfileCollectionByStartData().collectList().block();
            //Заполнить коллекцию чат-переписки начальными данными
            this.fillCollectionByUserPairsStartData(new ChatMessageSaver(this.chatMessageRepository)).collectList().block();
            //Заполнить коллекцию посетителей начальными данными
            this.fillCollectionByUserPairsStartData(new VisitorSaver(this.visitorRepository)).collectList().block();
        } else {
            log.info(MessageConstants.prefixMsg(MessageConstants.MSG_USER_PROFILE_COLLECTION_FILLED));
        }
    }


    /**
     * Заполняет парными записями коллекции для заполнения стартовыми данными
     * @param executor SingleExecutor<ChatMessage, ReactiveCrudRepository>
     * @return Flux<?>
     */
    private Flux<?> fillCollectionByUserPairsStartData(SingleSaver<?, ReactiveCrudRepository> executor) {
        if (this.userProfileRepository.count().block() != 0) {

            return this.userProfileRepository.findAll().doOnNext(profileItem -> {

                this.userProfileRepository.findAll().doOnNext(innerProfileItem -> {
                    if (!profileItem.equals(innerProfileItem)) {

                            executor.save(new String[] {
                                    profileItem.getId(),
                                    innerProfileItem.getId(),
                                    innerProfileItem.getFirstName()
                            }).subscribe();

                    }
                }).subscribe();
            }).delayElements(Duration.ofMillis(5));
        }
        return Flux.empty();
    }

    /**
     * Сохранить группу профилей
     * @param userProfiles List<UserProfile>
     */
    private Flux<UserProfile> saveUserProfiles(List<UserProfile> userProfiles) {
       return this.userProfileRepository.saveAll(userProfiles).doOnError(s -> {
            log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_CANT_SAVE_USER, s.getMessage())));
        }).doOnNext(s -> {
            log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_SAVED_USER, s.getFirstName())));
        });
    }



}
