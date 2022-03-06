package com.romanm.jwtservicedata.repositories;

import com.romanm.jwtservicedata.models.ChatItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface ChatMessageRepository extends ReactiveCrudRepository<ChatItem, String> {
    Flux<ChatItem> findChatMessageByUserId(String userId);
    Flux<ChatItem> findChatItemsByIdIn(List<String> ids);
    Flux<ChatItem> findChatMessageByUserIdAndFromUserId(String userId, String fromUserId);
}
