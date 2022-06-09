package com.romanm.jwtservicedata.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.romanm.jwtservicedata.constants.CommonConstants;
import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = CommonConstants.CHAT_MESSAGE_COLLECTION)
public class ChatItem implements Serializable {
    @Id
    @NotNull
    private String id;
    @NotNull
    @NotBlank
    @Indexed(name = "userId")
    private String userId;
    @NotNull
    @NotBlank
    @Indexed(name = "fromUserId")
    private String fromUserId;
    @NotBlank
    private String message;
    @Indexed(name = "timestamp")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date timestamp;
    private long group = 0;
    private boolean isEmailed = false;
    private boolean isRead = false;

    public ChatItem(String userId, String fromUserId, String message) {
        this.message = message;
        this.userId = userId;
        this.fromUserId = fromUserId;
        this.timestamp = new Date();
        this.isEmailed = false;
        this.isRead = false;
        this.group = 0;
    }

    public ChatItem(String userId, String fromUserId, String message, long group) {
        this.message = message;
        this.userId = userId;
        this.fromUserId = fromUserId;
        this.group = group;
        this.timestamp = new Date();
        this.isEmailed = false;
        this.isRead = false;
    }

    public ChatItem(String userId, String fromUserId, String message, Date currentDate) {
        this.message = message;
        this.userId = userId;
        this.fromUserId = fromUserId;
        this.timestamp = currentDate;
        this.isEmailed = false;
        this.isRead = false;
        this.group = 0;
    }

    public ChatItem(String userId, String fromUserId, String message, Date currentDate, long group) {
        this.message = message;
        this.userId = userId;
        this.fromUserId = fromUserId;
        this.timestamp = currentDate;
        this.group = group;
        this.isEmailed = false;
        this.isRead = false;
    }
}
