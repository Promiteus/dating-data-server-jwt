package com.romanm.jwtservicedata.services.interfaces;

import com.romanm.jwtservicedata.models.ChatItem;
import com.romanm.jwtservicedata.models.requests.MessageApplier;
import com.romanm.jwtservicedata.models.responses.MessageApplierResponse;
import org.springframework.data.domain.Sort;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;


public interface ChatService {
    Mono<ChatItem> saveMessage(ChatItem chatMessage);
    Flux<ChatItem> findMessages(String userId, String fromUserId, int page, int size, Sort.Direction direction);
    Flux<ChatItem> findUsersMessages(String userId1, String userId2, int page, int size, Sort.Direction direction);
    Flux<ChatItem> findUsersMessagesMerged(String userId1, String userId2, int page, int size, Sort.Direction direction);
    Mono<ChatItem> addMessage(String toUserId, String fromUserId, String message);
    Flux<ChatItem> saveMessages(List<ChatItem> chatMessages);
    Flux<ChatItem> findMessagesByIds(List<String> messageIds);
    Mono<MessageApplierResponse> appliedMessages(MessageApplier messageApplier);
}
