package com.romanm.jwtservicedata.models.responses.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.models.UserProfile;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = CommonConstants.VISITOR_COLLECTION)
public class ResponseUserProfile {
    private UserProfile userProfile; //Профиль пользователя
    private List<UserProfile> lastVisitors = new ArrayList<>(); //Список последних 10 посетителей
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ResponseUserProfile(UserProfile userProfile, List<UserProfile> lastVisitors) {
        this.userProfile = userProfile;
        this.lastVisitors = lastVisitors;

        this.timestamp = LocalDateTime.now();
    }

}
