package com.romanm.jwtservicedata.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.models.images.ImageRef;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = CommonConstants.USER_PROFILE_COLLECTION)
public class UserProfile implements Serializable {
    @Id
    @NotNull
    private String id; //код пользователя по регистрации
    @NotBlank
    private String firstName; //Имя
    private String lastName; //Фамилия
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthDate; //Год рождения
    private int height; //Рост
    private int weight; //Масса тела
    @Size(max = 1000)
    private String aboutMe; //О себе
    private String hobby; //Увлечения
   // @Indexed(useGeneratedName = true)
    private int kids = 0; //Количество детей
    private CommonConstants.FamilyStatus familyStatus = CommonConstants.FamilyStatus.SINGLE; //Семейное положение
    private long rank = 1000; //Ранг по позиции анкеты
    @Indexed(useGeneratedName = true)
    private CommonConstants.SexOrientation sexOrientation = CommonConstants.SexOrientation.HETERO; //Суксуальная ориентация
    @Indexed(useGeneratedName = true)
    private CommonConstants.MeetPreferences meetPreferences = CommonConstants.MeetPreferences.ALL; //Предпочитаю знакомиться
    @Indexed(useGeneratedName = true)
    private CommonConstants.Sex sex = CommonConstants.Sex.MAN; //Пол
    private Set<ImageRef> imgUrls = new HashSet<>(); //Список url изображений
    private ImageRef thumbUrl = new ImageRef(); //Url привью профиля пользователя
}
