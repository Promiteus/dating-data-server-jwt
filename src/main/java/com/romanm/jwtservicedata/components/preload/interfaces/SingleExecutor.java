package com.romanm.jwtservicedata.components.preload.interfaces;

import com.romanm.jwtservicedata.models.ChatMessage;
import reactor.core.publisher.Flux;

public interface SingleExecutor<T, R> {
    Flux<ChatMessage> execute(String[] args);
}
