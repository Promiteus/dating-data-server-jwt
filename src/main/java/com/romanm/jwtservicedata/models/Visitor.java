package com.romanm.jwtservicedata.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.romanm.jwtservicedata.constants.CommonConstants;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document(collection = CommonConstants.VISITOR_COLLECTION)
public class Visitor implements Serializable {
    @Id
    @NotNull
    private String id;
    @Indexed(name = "userId")
    private String userId;
    @Indexed(name = "visitorUserId")
    private String visitorUserId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public Visitor(String userId, String visitorUserId) {
        this.userId = userId;
        this.visitorUserId = visitorUserId;

        this.timestamp = LocalDateTime.now();
    }

    public static String getVisitorUserIdFieldName() {
        return Visitor.class.getDeclaredFields()[1].getName();
    }

    public static String getVisitorVisitorUserIdFieldName() {
        return Visitor.class.getDeclaredFields()[2].getName();
    }

    public static String getVisitorTimestampFieldName() {
        return Visitor.class.getDeclaredFields()[3].getName();
    }
}
