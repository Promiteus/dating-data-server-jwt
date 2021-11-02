package com.romanm.jwtservicedata.services.interfaces;

import com.romanm.jwtservicedata.models.ChatMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IChatService {
    public Mono<ChatMessage> saveMessage(ChatMessage chatMessage);
    public Mono<List<ChatMessage>> findMessages(String userId);
}
