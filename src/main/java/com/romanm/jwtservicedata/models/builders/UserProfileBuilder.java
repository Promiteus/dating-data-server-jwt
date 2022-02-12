package com.romanm.jwtservicedata.models.builders;

import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.models.UserProfile;

import java.util.Date;

public class UserProfileBuilder {

    private static UserProfile userProfile;
    private static UserProfileBuilder userProfileBuilder;

    private UserProfileBuilder() {
        userProfile = new UserProfile();
    }

    public static UserProfileBuilder create(String id) {
        userProfileBuilder = new UserProfileBuilder();
        userProfile.setId(id);
        return userProfileBuilder;
    }

    public UserProfileBuilder setFirstName(String firstName) {
        userProfile.setFirstName(firstName);
        return userProfileBuilder;
    }

    public UserProfileBuilder setAge(int age) {
        userProfile.setAge(age);
        return userProfileBuilder;
    }

    public UserProfileBuilder setLastName(String lastName) {
        userProfile.setLastName(lastName);
        return userProfileBuilder;
    }

    public UserProfileBuilder setBirthDate(Date birthDate) {
        userProfile.setBirthDate(birthDate);
        return userProfileBuilder;
    }

    public UserProfileBuilder setHeight(int height) {
        userProfile.setHeight(height);
        return userProfileBuilder;
    }

    public UserProfileBuilder setWeight(int weight) {
        userProfile.setWeight(weight);
        return userProfileBuilder;
    }

    public UserProfileBuilder setAboutMe(String about) {
        userProfile.setAboutMe(about);
        return userProfileBuilder;
    }

    public UserProfileBuilder setKids(int kids) {
        userProfile.setKids(kids);
        return userProfileBuilder;
    }

    public UserProfileBuilder setFamilyStatus(CommonConstants.FamilyStatus familyStatus) {
        userProfile.setFamilyStatus(familyStatus);
        return userProfileBuilder;
    }

    public UserProfileBuilder setSex(CommonConstants.Sex sex) {
        userProfile.setSex(sex);
        return userProfileBuilder;
    }

    public UserProfileBuilder setSexOrientation(CommonConstants.SexOrientation orientation) {
        userProfile.setSexOrientation(orientation);
        return userProfileBuilder;
    }

    public UserProfileBuilder setRank(int rank) {
        userProfile.setRank(rank);
        return userProfileBuilder;
    }

    public UserProfile build() {
        return userProfile;
    }
}
