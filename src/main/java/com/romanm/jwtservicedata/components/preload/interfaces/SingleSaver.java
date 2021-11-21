package com.romanm.jwtservicedata.components.preload.interfaces;

import reactor.core.publisher.Mono;

public interface SingleSaver<T, R> {
   Mono<T> save(String[] args);
}
