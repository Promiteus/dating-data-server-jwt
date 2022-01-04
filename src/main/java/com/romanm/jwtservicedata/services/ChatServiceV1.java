package com.romanm.jwtservicedata.services;

import com.romanm.jwtservicedata.models.ChatItem;
import com.romanm.jwtservicedata.repositories.ChatMessageRepository;
import com.romanm.jwtservicedata.repositories.pageble.ChatMessagePageRepository;
import com.romanm.jwtservicedata.services.interfaces.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service("chatServiceV1")
public class ChatServiceV1 implements ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessagePageRepository chatMessagePageRepository;

    @Autowired
    public ChatServiceV1(ChatMessageRepository chatMessageRepository, ChatMessagePageRepository chatMessagePageRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessagePageRepository = chatMessagePageRepository;
    }

    @Override
    public Mono<ChatItem> saveMessage(ChatItem chatMessage) {
        return this.chatMessageRepository.save(chatMessage);
    }

    @Override
    public Flux<ChatItem> findMessages(String userId, String fromUserId, int page, int size, Sort.Direction direction) {
        return this.chatMessagePageRepository.findChatMessageByUserIdAndFromUserIdOrderByTimestampDesc(userId, fromUserId, PageRequest.of(page, size));
    }

    @Override
    public Flux<ChatItem> findUsersMessages(String userId1, String userId2, int page, int size, Sort.Direction direction) {
        return this.chatMessagePageRepository.findChatMessageByUserIdAndFromUserIdOrderByTimestampDesc(userId1, userId2, PageRequest.of(page, size))
                .concatWith(this.chatMessagePageRepository.findChatMessageByUserIdAndFromUserIdOrderByTimestampDesc(userId2, userId1, PageRequest.of(page, size)))
                .sort((s1, s2) -> {
                    return s1.getTimestamp().compareTo(s2.getTimestamp());
                });
    }


}
