package com.romanm.jwtservicedata.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.romanm.jwtservicedata.constants.CommonConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = CommonConstants.CHAT_MESSAGE_COLLECTION)
public class ChatMessage implements Serializable {
    @Id
    @NotNull
    private String id;
    @NotNull
    @NotBlank
    private String userId;
    @NotNull
    @NotBlank
    private String fromUserId;
    @NotBlank
    private String message;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ChatMessage(String userId, String fromUserId, String message) {
        this.message = message;
        this.userId = userId;
        this.fromUserId = fromUserId;
        this.timestamp = LocalDateTime.now();
    }
}
