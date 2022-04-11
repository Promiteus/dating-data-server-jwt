package com.romanm.jwtservicedata.components.preload;

import com.romanm.jwtservicedata.components.preload.interfaces.SingleSaver;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.ChatItem;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@SuppressWarnings("rawtypes")
public class ChatMessageSaver implements SingleSaver<List<ChatItem>, ReactiveCrudRepository> {

    private ReactiveCrudRepository r;

    /**
     * Конструктор класса ChatMessageSaver
     * @param r ReactiveCrudRepository
     */
    public ChatMessageSaver(ReactiveCrudRepository r) {
        this.r = r;
    }

    /**
     * Сохранение группы сообщений
     * @param args String[]
     * @return Mono<List<ChatItem>>
     */
    @Override
    public Mono<List<ChatItem>> save(String[] args) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        List<ChatItem> chatMessageList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            calendar.add(Calendar.SECOND, 30);
            chatMessageList.add(
                    new ChatItem(
                            args[0], args[1], String.format(MessageConstants.MSG_CHAT_MESSAGE_FROM_USER, args[2], i), calendar.getTime(), 0
                    )
            );
        }
        return Mono.from(r.saveAll(chatMessageList));
    }
}
