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
    private int age = 18;
    private CommonConstants.SexOrientation sexOrientation;
    private CommonConstants.MeetPreferences meetPreferences;
    private CommonConstants.Sex sex;
    private CommonConstants.FamilyStatus familyStatus;
    private String country = "Россия"; //Страна
    private String region = ""; //Регион
    private String locality = ""; //Нас. пункт
}
