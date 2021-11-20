package com.romanm.jwtservicedata.components.preload;

import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.ChatMessage;
import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.builders.UserProfileBuilder;
import com.romanm.jwtservicedata.repositories.ChatMessageRepository;
import com.romanm.jwtservicedata.repositories.UserProfileRepository;
import com.romanm.jwtservicedata.repositories.VisitorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

    public void fillUserProfileCollectionByStartData() {
        Calendar c = Calendar.getInstance();

        if (this.userProfileRepository.count().block() == 0) {
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

            this.saveUserProfiles(userProfiles).collectList().block();
            this.fillChatMessagesCollectionByStartData().collectList().block();
        } else {
            log.info(MessageConstants.prefixMsg(MessageConstants.MSG_USER_PROFILE_COLLECTION_FILLED));
        }
    }

    public void fillStarterData() {

    }

    public Flux<UserProfile> fillChatMessagesCollectionByStartData() {
        if (this.chatMessageRepository.count().block() == 0) {

          return this.userProfileRepository.findAll().doOnNext(profileItem -> {

               this.userProfileRepository.findAll().doOnNext(innerProfileItem -> {
                   if (!profileItem.equals(innerProfileItem)) {
                       log.info(profileItem.getId()+" >> "+innerProfileItem.getId());
                       for (int i = 0; i < 20; i++) {
                           this.chatMessageRepository.save(
                                   new ChatMessage(
                                           profileItem.getId(),
                                           innerProfileItem.getId(),
                                           String.format(
                                                   MessageConstants.MSG_CHAT_MESSAGE_FROM_USER,
                                                   innerProfileItem.getFirstName(),
                                                   i)
                                   )
                           ).subscribe();
                       }
                   }
               }).subscribe();
            });
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

    /**
     * Сохранить группу сообщений
     * @param chatMessages List<ChatMessage>
     */
    private Flux<ChatMessage> saveChatMessages(List<ChatMessage> chatMessages) {
       return this.chatMessageRepository.saveAll(chatMessages).doOnError(s -> {
            log.info(MessageConstants.prefixMsg(String.format(MessageConstants.MSG_CANT_SAVE_CHAT_MESSAGES, s.getMessage())));
       });
    }

}
