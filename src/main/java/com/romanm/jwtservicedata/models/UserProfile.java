package com.romanm.jwtservicedata.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.romanm.jwtservicedata.constants.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Document(collection = CommonConstants.USER_PROFILE_COLLECTION)
public class UserProfile implements Serializable {
    @Id
    @NotNull
    //@Indexed(unique = true)
    private String userId; //код пользователя по регистрации
    @NotBlank
    private String firstName; //Имя
    private String lastName; //Фамилия
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthDate; //Год рождения
    private int height; //Рост
    private int weight; //Масса тела
    @Size(max = 1000)
    private String aboutMe; //О себе
    private int kids = 0; //Количество детей
    private CommonConstants.FamilyStatus familyStatus = CommonConstants.FamilyStatus.SINGLE; //Семейное положение
    private long rank = 1000; //Ранг по позиции анкеты
    private CommonConstants.SexOrientation sexOrientation = CommonConstants.SexOrientation.HETERO; //Суксуальная ориентация
    private CommonConstants.Sex sex = CommonConstants.Sex.MAN; //Пол
}
