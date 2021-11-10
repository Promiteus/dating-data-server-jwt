package com.romanm.jwtservicedata.services;

import com.romanm.jwtservicedata.models.ChatMessage;
import com.romanm.jwtservicedata.repositories.ChatMessageRepository;
import com.romanm.jwtservicedata.services.interfaces.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service("chatServiceV1")
public class ChatServiceV1 implements IChatService {
    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatServiceV1(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public Mono<ChatMessage> saveMessage(ChatMessage chatMessage) {
        return this.chatMessageRepository.save(chatMessage);
    }

    @Override
    public Flux<ChatMessage> findMessages(String userId, int page, int size, Sort.Direction direction) {
        return this.chatMessageRepository.findChatMessageByUserId(userId);
    }


}
