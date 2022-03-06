package com.romanm.jwtservicedata.services;

import com.romanm.jwtservicedata.models.ChatItem;
import com.romanm.jwtservicedata.repositories.ChatMessageRepository;
import com.romanm.jwtservicedata.repositories.pageble.ChatMessagePageRepository;
import com.romanm.jwtservicedata.services.interfaces.ChatService;
import com.romanm.jwtservicedata.services.mongodb.MongoOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;


@Service("chatServiceV1")
public class ChatServiceV1 implements ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessagePageRepository chatMessagePageRepository;
    private final MongoOperations mongoOperations;

    @Autowired
    public ChatServiceV1(
            ChatMessageRepository chatMessageRepository,
            ChatMessagePageRepository chatMessagePageRepository,
            MongoOperations mongoOperations) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatMessagePageRepository = chatMessagePageRepository;
        this.mongoOperations = mongoOperations;
    }

    /**
     * Метод добавления сообщения объекту
     * @param chatMessage ChatItem
     * @return Mono<ChatItem>
     */
    @Override
    public Mono<ChatItem> saveMessage(ChatItem chatMessage) {
        return this.chatMessageRepository.save(chatMessage);
    }

    /**
     * Сохранение группы сообщений
     * @param chatMessages List<ChatItem>
     * @return  Flux<ChatItem>
     */
    @Override
    public Flux<ChatItem> saveMessages(List<ChatItem> chatMessages) {
        return this.chatMessageRepository.saveAll(chatMessages);
    }

    /**
     * Получить статусы о прочтении указанных списком id сообщений
     * @param messageIds List<String>
     * @return Flux<ChatItem>
     */
    @Override
    public Flux<ChatItem> findMessagesByIds(List<String> messageIds) {
        if (messageIds.size() > 0) {
            return this.chatMessageRepository.findChatItemsByIdIn(messageIds);
        }
        return Flux.empty();
    }

    /**
     * Найти сообщения по конкретным параметрам
     * @param userId String
     * @param fromUserId String
     * @param page int
     * @param size int
     * @param direction Sort.Direction
     * @return Flux<ChatItem>
     */
    @Override
    public Flux<ChatItem> findMessages(String userId, String fromUserId, int page, int size, Sort.Direction direction) {
        return this.chatMessagePageRepository.findChatMessageByUserIdAndFromUserIdOrderByTimestampDesc(userId, fromUserId, PageRequest.of(page, size));
    }

    @Override
    public Flux<ChatItem> findUsersMessages(String userId1, String userId2, int page, int size, Sort.Direction direction) {
        return this.mongoOperations.getCurrentProfileChatCorrespondence(userId1, userId2, page, size, direction)
                .concatWith(mongoOperations.getCurrentProfileChatCorrespondence(userId2, userId1, page, size, direction))
                .sort((s1, s2) -> {
                    return s1.getTimestamp().compareTo(s2.getTimestamp());
                });
    }

    /**
     *
     * @param userId1 String
     * @param userId2 String
     * @param page int
     * @param size int
     * @param direction Sort.Direction
     * @return Flux<ChatItem>
     */
    @Override
    public Flux<ChatItem> findUsersMessagesMerged(String userId1, String userId2, int page, int size, Sort.Direction direction) {
        return Flux.mergeSequential(
                this.mongoOperations.getCurrentProfileChatCorrespondence(userId1, userId2, page, size, direction),
                this.mongoOperations.getCurrentProfileChatCorrespondence(userId2, userId1, page, size, direction)
        ).sort(Comparator.comparing(ChatItem::getTimestamp));
    }

    /**
     * Метод добавления сообщения в чат по параметрам
     * @param toUserId String
     * @param fromUserId String
     * @param message String
     * @return Mono<ChatItem>
     */
    @Override
    public Mono<ChatItem> addMessage(String toUserId, String fromUserId, String message) {
        return this.chatMessageRepository.save(new ChatItem(toUserId, fromUserId, message));
    }


}
