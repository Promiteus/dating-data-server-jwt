package com.romanm.jwtservicedata.components.preload;

import com.romanm.jwtservicedata.components.preload.interfaces.SingleSaver;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.ChatMessage;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ChatMessageSaver implements SingleSaver<List<ChatMessage>, ReactiveCrudRepository> {

    private ReactiveCrudRepository r;

    public ChatMessageSaver(ReactiveCrudRepository r) {
        this.r = r;
    }

    @Override
    public Mono<List<ChatMessage>> save(String[] args) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        List<ChatMessage> chatMessageList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            calendar.add(Calendar.SECOND, 30);
            chatMessageList.add(
                    new ChatMessage(
                            args[0], args[1], String.format(MessageConstants.MSG_CHAT_MESSAGE_FROM_USER, args[2], i), calendar.getTime()
                    )
            );
        }
        return Mono.from(r.saveAll(chatMessageList));
    }
}
