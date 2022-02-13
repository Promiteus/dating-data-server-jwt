package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.models.ChatItem;
import com.romanm.jwtservicedata.models.responses.ResponseData;
import com.romanm.jwtservicedata.services.interfaces.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping(value = Api.API_PREFIX)
public class ChatMessageController {
    @Qualifier(value = "chatServiceV1")
    private final ChatService chatService;

    @Autowired
    public ChatMessageController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping(value = Api.API_CHAT_MESSAGES)
    public ResponseEntity<Mono<ResponseData<ChatItem>>> getChatMessages(@RequestParam(value = Api.PARAM_PAGE, defaultValue = "0", required = false) int page,
                                                                        @RequestParam(value = Api.PARAM_PAGE_SIZE, defaultValue = "10", required = false) int pageSize,
                                                                        @RequestParam(value = Api.PARAM_USER_ID, defaultValue = "", required = true) String userId,
                                                                        @RequestParam(value = Api.PARAM_FROM_USER_ID, defaultValue = "", required = true) String fromUserId) {

        return ResponseEntity.ok(this.chatService
                .findMessages(userId, fromUserId, page, pageSize, Sort.Direction.ASC)
                .collectList()
                .map(data -> {
                      return new ResponseData<>(page, pageSize, data);
                }));
    }

    @GetMapping(value = Api.API_CHAT_USERS_MESSAGES)
    public ResponseEntity<Mono<ResponseData<ChatItem>>> getUsersChatMessages(@RequestParam(value = Api.PARAM_PAGE, defaultValue = "0", required = false) int page,
                                                                             @RequestParam(value = Api.PARAM_PAGE_SIZE, defaultValue = "10", required = false) int pageSize,
                                                                             @RequestParam(value = Api.PARAM_USER_ID, defaultValue = "", required = true) String userId,
                                                                             @RequestParam(value = Api.PARAM_FROM_USER_ID, defaultValue = "", required = true) String fromUserId) {

        return ResponseEntity.ok(this.chatService
                .findUsersMessages(userId, fromUserId, page, pageSize, Sort.Direction.ASC)
                .collectList()
                .map(data -> {
                    return new ResponseData<>(page, pageSize, data);
                }));
    }

    @PostMapping(value = Api.API_CHAT_ADD_ITEM)
    public ResponseEntity<Mono<ChatItem>> storeChatItem(@RequestBody ChatItem chatItem) {
        if (chatItem.getTimestamp() == null) {
            chatItem.setTimestamp(new Date());
        }
        return ResponseEntity.ok(this.chatService.saveMessage(chatItem));
    }
}
