package com.romanm.jwtservicedata.repositories.pageble;


import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.ChatMessage;
import com.romanm.jwtservicedata.repositories.ChatMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@ActiveProfiles(value = {"test"})
public class ChatMessagePageRepositoryTest {
    @Autowired
    private ChatMessagePageRepository chatMessagePageRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Test
    public void chatMessagePageTest() {
       List<ChatMessage> chatMessageList = new ArrayList<>();

       int controlPageSize = 30;
       chatMessageList = this.chatMessagePageRepository.findChatMessageByUserIdAndFromUserId("200", "201", PageRequest.of(0, controlPageSize)).collectList().block();

       if ((chatMessageList != null) && (chatMessageList.size() > 0)) {

           int pageSize = 10;
           this.chatMessagePageRepository
                   .findChatMessageByUserIdAndFromUserId("200", "201", PageRequest.of(0, pageSize))
                   .collectList()
                   .doOnSuccess(s -> {
                        s.forEach(item -> {
                            log.info(MessageConstants.prefixMsg(MessageConstants.prefixMsg(String.format("Got message %s <- %s: %s", item.getUserId(), item.getFromUserId(), item.getMessage()))));
                        });
                        log.info(MessageConstants.prefixMsg("Got amount of messages between userId 200 and 201: "+s.size()));
                        Assert.assertEquals(pageSize, s.size());
                   }).subscribe();
       }
    }
}