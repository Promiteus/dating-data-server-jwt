package com.romanm.jwtservicedata.components.preload;

import com.romanm.jwtservicedata.components.preload.interfaces.SingleExecutor;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.ChatMessage;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ChatMessageSaver implements SingleExecutor<ChatMessage, ReactiveCrudRepository> {

    private ReactiveCrudRepository r;

    public ChatMessageSaver(ReactiveCrudRepository r) {
        this.r = r;
    }

    @Override
    public Flux<ChatMessage> execute(String[] args) {
        List<ChatMessage> chatMessageList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            chatMessageList.add(
                    new ChatMessage(
                            args[0], args[1], String.format(MessageConstants.MSG_CHAT_MESSAGE_FROM_USER, args[2], i)
                    )
            );
        }
        return r.saveAll(chatMessageList);
    }
}
