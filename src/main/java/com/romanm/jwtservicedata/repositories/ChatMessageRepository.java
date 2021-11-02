package com.romanm.jwtservicedata.repositories;

import com.romanm.jwtservicedata.models.ChatMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {
    Mono<List<ChatMessage>> findChatMessageByUserId(String userId);
}
