package com.romanm.jwtservicedata.services.interfaces;

import com.romanm.jwtservicedata.models.Visitor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VisitorService {
    Flux<Visitor> findPagebleVisitorsByUserId(String userId, int page, int pageSize);
    Mono<Visitor> updateVisitor(String userId, String visitorUserId);
}
