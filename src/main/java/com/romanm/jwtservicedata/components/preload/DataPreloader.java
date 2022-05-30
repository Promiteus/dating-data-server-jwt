package com.romanm.jwtservicedata.components.preload;

import com.romanm.jwtservicedata.components.preload.interfaces.SingleSaver;
import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.builders.UserProfileBuilder;
import com.romanm.jwtservicedata.models.responses.files.FileStatus;
import com.romanm.jwtservicedata.repositories.ChatMessageRepository;
import com.romanm.jwtservicedata.repositories.UserProfileRepository;
import com.romanm.jwtservicedata.repositories.VisitorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс для загрузки стартовых коллекций для mongodb
 */
@Slf4j
@Component
public class DataPreloader {
    private int userId = 200;

    private static final String SAVED_SINGLE_FILE = "Saved single image '%s'!";
    private static final String CANT_SAVE_SINGLE_FILE = "Can't save single image '%s'!";
    private static final String SAVED_SINGLE_THUMB_FILE = "Saved thumb image '%s'!";
    private static final String CANT_SAVE_THUMB_FILE = "Can't save thumb image '%s'!";

    @Autowired
    private UserProfileRepository userProfileRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private VisitorRepository visitorRepository;

    @Value("classpath:test3/*")
    Resource[] resourceFilesTest3;

    /**
     * Заполнить коллекцию тестовыми обезличенными профилями
     * @return Flux<UserProfile>
     */
    private Flux<UserProfile> fillUserProfileCollectionByStartDataDef() {
        Calendar c = Calendar.getInstance();
        List<UserProfile> userProfiles = new ArrayList<>();

        int yearStep = 2005;
        CommonConstants.Sex sex = CommonConstants.Sex.MAN;
        int kids = 0;
        for (int i = 0; i < 260; i++) {
            yearStep -= ((i % 10) == 0) ? 1: 0; //Прибавлять возраст на каждом 10-м пользователя
            c.set(yearStep , Calendar.FEBRUARY, 2, 0, 0);

            kids = ((i % 2) == 0) ? 1: 0; //Высавлять флага ребенка на каждом втором пользователе
            sex = ((i % 2) == 0) ? CommonConstants.Sex.MAN: CommonConstants.Sex.WOMAN; //Высавлять по мужчины на каждом втором пользователе

            UserProfile profile = UserProfileBuilder.create("2000"+i)
                    .setFirstName("Имя "+i)
                    .setLastName("Фамилия "+i)
                    .setBirthDate(c.getTime())
                    .setKids(kids)
                    .setWeight(68)
                    .setAge(LocalDateTime.now().getYear() - yearStep)
                    .setHeight(170)
                    .setAboutMe("О пользователе "+i)
                    .setFamilyStatus(CommonConstants.FamilyStatus.SINGLE)
                    .setSex(sex)
                    .setSexOrientation(CommonConstants.SexOrientation.HETERO)
                    .setRank(2000).build();
            userProfiles.add(profile);
        }

        return this.saveUserProfiles(userProfiles);
    }

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
                    .setAge(LocalDateTime.now().getYear() - c.get(Calendar.YEAR))
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
                    .setAge(LocalDateTime.now().getYear() - c.get(Calendar.YEAR))
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
                    .setAge(LocalDateTime.now().getYear() - c.get(Calendar.YEAR))
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
                    .setAge(LocalDateTime.now().getYear() - c.get(Calendar.YEAR))
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
            //Заполнить коллекцию тестовыми обезличенными профилями
            this.fillUserProfileCollectionByStartDataDef().collectList().block();
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
