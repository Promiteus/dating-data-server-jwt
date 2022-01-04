package com.romanm.jwtservicedata.services.interfaces;

import com.romanm.jwtservicedata.models.ChatItem;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ChatService {
    public Mono<ChatItem> saveMessage(ChatItem chatMessage);
    public Flux<ChatItem> findMessages(String userId, String fromUserId, int page, int size, Sort.Direction direction);
    public Flux<ChatItem> findUsersMessages(String userId1, String userId2, int page, int size, Sort.Direction direction);
}
