package com.romanm.jwtservicedata.models.requests;

import com.romanm.jwtservicedata.constants.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchBody implements Serializable {
    private int kids = 0;
    private int ageFrom = 18;
    private int ageTo = 50;
    private CommonConstants.SexOrientation sexOrientation = CommonConstants.SexOrientation.HETERO;
    private CommonConstants.MeetPreferences meetPreferences = CommonConstants.MeetPreferences.ALL;
    private CommonConstants.Sex sex = CommonConstants.Sex.MAN;
    private CommonConstants.FamilyStatus familyStatus = CommonConstants.FamilyStatus.SINGLE;
    private String country = "Россия"; //Страна
    private String region = ""; //Регион
    private String locality = ""; //Нас. пункт
}
