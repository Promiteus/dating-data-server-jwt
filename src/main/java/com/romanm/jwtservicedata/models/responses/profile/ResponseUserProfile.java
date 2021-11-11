package com.romanm.jwtservicedata.models.responses.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.romanm.jwtservicedata.models.UserProfile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ResponseUserProfile {
    private UserProfile userProfile; //Профиль пользователя
    private List<UserProfile> lastVisitors; //Список последних 10 посетителей
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ResponseUserProfile(UserProfile userProfile, List<UserProfile> lastVisitors) {
        this.userProfile = userProfile;
        this.lastVisitors = lastVisitors;

        this.timestamp = LocalDateTime.now();
    }

    public ResponseUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
        this.lastVisitors = new ArrayList<>();

        this.timestamp = LocalDateTime.now();
    }

}
