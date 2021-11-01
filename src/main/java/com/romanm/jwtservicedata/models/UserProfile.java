package com.romanm.jwtservicedata.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Document(collection = "user_profiles")
public class UserProfile {
    public enum FamilyStatus {
        SINGLE, MARRIED
    };

    @Id
    @NotNull
    private String id;

    private String firstName;

    private String secondName;

    private Date birthDate;

    private int height;

    private int weight;

    private String aboutMe;

    private int kids = 0;

    private FamilyStatus familyStatus = FamilyStatus.SINGLE;

    public UserProfile() {}

}
