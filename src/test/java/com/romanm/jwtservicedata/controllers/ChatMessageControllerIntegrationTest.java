package com.romanm.jwtservicedata.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanm.jwtservicedata.configs.auth.TestSecurityConfiguration;
import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.ChatMessage;
import com.romanm.jwtservicedata.models.responses.ResponseData;
import com.romanm.jwtservicedata.repositories.pageble.ChatMessagePageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(value = {"test"})
@Import(value = TestSecurityConfiguration.class)
public class ChatMessageControllerIntegrationTest {
    @Autowired
    private ChatMessagePageRepository chatMessagePageRepository;

    @Autowired
    private WebTestClient webTestClient;

    private final static String TEST_USER_ID = "209";
    private final static String TEST_FROM_USER_ID = "209";

    @Test
    public void getLastPageMessagesTest() {
        int pageSize = 10;
        String messageContent = "Message %d";

        List<ChatMessage> chatMessageList = new ArrayList<>();

        for (int i = 0; i < pageSize; i++) {
            ChatMessage chatMessage = new ChatMessage(TEST_USER_ID, TEST_FROM_USER_ID, String.format(messageContent, i));
            chatMessageList.add(chatMessage);
            this.chatMessagePageRepository.save(chatMessage).delayElement(Duration.ofMillis(800)).block();
        }

        this.webTestClient
                .get()
                .uri(uriBuilder -> (
                        uriBuilder.path(Api.API_PREFIX+Api.API_CHAT_MESSAGES)
                                .queryParam(Api.PARAM_PAGE, 0)
                                .queryParam(Api.PARAM_PAGE_SIZE, pageSize)
                                .queryParam(Api.PARAM_USER_ID, TEST_USER_ID)
                                .queryParam(Api.PARAM_FROM_USER_ID, TEST_FROM_USER_ID)
                                .build()))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ResponseData.class)
                .value(data -> {
                    log.info(MessageConstants.prefixMsg("Got chat data: "+data.getData()));
                    log.info(MessageConstants.prefixMsg("Got chat responseData: "+data));

                    try {
                        try {
                            String jsonFirst =
                                    new ObjectMapper().writeValueAsString(data.getData().get(0));
                            Map<String, String> mapFirst = new ObjectMapper().readValue(jsonFirst, HashMap.class);
                            String jsonLast =
                                    new ObjectMapper().writeValueAsString(data.getData().get(pageSize-1));
                            Map<String, String> mapLast = new ObjectMapper().readValue(jsonLast, HashMap.class);

                            log.info(MessageConstants.prefixMsg("Got chat first message: "+mapFirst.get("message")));
                            log.info(MessageConstants.prefixMsg("Got chat last message: "+mapLast.get("message")));

                            Assert.assertEquals(String.format(messageContent, pageSize-1), mapFirst.get("message"));
                            Assert.assertEquals(String.format(messageContent, 0), mapLast.get("message"));
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    } finally {
                        //Удалить тестовые записи
                        this.chatMessagePageRepository.deleteAll(chatMessageList).subscribe();
                    }

                });
    }
}
