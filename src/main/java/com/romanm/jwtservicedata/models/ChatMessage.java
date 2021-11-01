package com.romanm.jwtservicedata.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;

@Data
@Document(collection = "chat_messages")
public class ChatMessage {
    @Id
    @NotNull
    private String id;
    @Indexed(unique = true)
    private String userId;
}
