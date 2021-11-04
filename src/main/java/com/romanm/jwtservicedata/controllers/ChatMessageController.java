package com.romanm.jwtservicedata.controllers;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.models.ChatMessage;
import com.romanm.jwtservicedata.services.interfaces.IChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = Api.API_PREFIX)
public class ChatMessageController {
    private final IChatService chatService;

    @Autowired
    public ChatMessageController(IChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping(value = Api.API_CHAT_MESSAGES)
    public ResponseEntity<Flux<ChatMessage>> getChatMessages(@RequestParam(value = Api.PARAM_PAGE, defaultValue = "0", required = false) int page,
                                                             @RequestParam(value = Api.PARAM_PAGE_SIZE, defaultValue = "10", required = false) int pageSize,
                                                             @RequestParam(value = Api.PARAM_USER_ID, defaultValue = "", required = true) String userId) {

        Flux<ChatMessage> chatMessages = this.chatService.findMessages(userId, page, pageSize, Sort.Direction.ASC);
        return null;
    }
}
