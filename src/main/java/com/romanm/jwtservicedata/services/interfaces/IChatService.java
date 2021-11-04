package com.romanm.jwtservicedata.services.interfaces;

import com.romanm.jwtservicedata.models.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface IChatService {
    public Mono<ChatMessage> saveMessage(ChatMessage chatMessage);
    public Flux<ChatMessage> findMessages(String userId, int page, int size, Sort.Direction direction);
}
