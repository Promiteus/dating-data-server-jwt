package com.romanm.jwtservicedata.models;

import com.romanm.jwtservicedata.constants.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "user_profiles")
public class UserProfile implements Serializable {
    @Id
    @NotNull
    private String id;

    private String firstName;

    private String lastName;

    private Date birthDate;

    private int height;

    private int weight;

    private String aboutMe;

    private int kids = 0;

    private CommonConstants.FamilyStatus familyStatus = CommonConstants.FamilyStatus.SINGLE;

    private long rank;


}
