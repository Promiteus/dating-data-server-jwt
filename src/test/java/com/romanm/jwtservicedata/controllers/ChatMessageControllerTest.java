package com.romanm.jwtservicedata.controllers;


import com.romanm.jwtservicedata.configs.auth.SecurityConfiguration;
import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.models.ChatMessage;
import com.romanm.jwtservicedata.services.ChatServiceV1;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import java.util.List;


@Slf4j
@RunWith(SpringRunner.class)
@WebFluxTest(controllers = ChatMessageController.class, excludeAutoConfiguration = {ReactiveSecurityAutoConfiguration.class})
public class ChatMessageControllerTest {
    @MockBean
    ChatServiceV1 chatService;

    @Autowired
    private WebTestClient webClient;

    @Test
    public void getChatMessagePage() {
        Flux<ChatMessage> messages = Flux.create(chatMessageFluxSink -> {
            for (int i = 0; i < 100; i++) {
                chatMessageFluxSink.next(new ChatMessage(Integer.toString(i), Integer.toString(i+1000), "message "+i));
            }
            chatMessageFluxSink.complete();
        });
       Mockito.when(this.chatService.findMessages("123", 0, 10, Sort.Direction.ASC))
                .thenReturn(messages);

       ChatMessage chatMessageFirst =  messages.blockFirst();
       List<ChatMessage> chatMessageList = messages.collectList().block();

       log.info("chatMessage first: "+chatMessageFirst);
       log.info("chatMessageList size: "+chatMessageList.size());

      this.webClient.get().uri(Api.API_PREFIX+Api.API_CHAT_MESSAGES).accept(MediaType.APPLICATION_JSON)
               .exchange().expectStatus().isOk();
    }
}