package com.romanm.jwtservicedata.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessage implements Serializable {
    @Id
    @NotNull
    private String id;
    @Indexed(unique = true)
    private String userId;
    private String message;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ChatMessage(String userId, String message) {
        this.message = message;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }
}
