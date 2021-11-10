package com.romanm.jwtservicedata.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Document(collection = "")
public class Visitor {
    @Id
    @NotNull
    private String id;

    private String userId;

    private String visitorUserId;

    private String likeUserId;

    public Visitor(String userId, String visitorUserId, String likeUserId) {
        this.userId = userId;
        this.visitorUserId = visitorUserId;
        this.likeUserId = likeUserId;
    }
}
