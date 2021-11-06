package com.romanm.jwtservicedata.repositories;

import com.romanm.jwtservicedata.models.ChatMessage;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatMessageRepository extends ReactiveCrudRepository<ChatMessage, String> {
    Flux<ChatMessage> findChatMessageByUserId(String userId);
}
