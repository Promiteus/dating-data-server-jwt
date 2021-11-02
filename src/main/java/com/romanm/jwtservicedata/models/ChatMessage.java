package com.romanm.jwtservicedata.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Document(collection = "chat_messages")
public class ChatMessage implements Serializable {
    @Id
    @NotNull
    private String id;
    @Indexed(unique = true)
    private String userId;
    private String message;
    private LocalDateTime localDateTime;

    public ChatMessage() {}

    public ChatMessage(String id, String userId, String message) {
        this.id = id;
        this.userId = userId;
        this.message = message;
        this.localDateTime = LocalDateTime.now();
    }
}
