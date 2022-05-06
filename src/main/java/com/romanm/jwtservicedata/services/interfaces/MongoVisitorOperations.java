package com.romanm.jwtservicedata.services.interfaces;

import com.romanm.jwtservicedata.models.Visitor;
import reactor.core.publisher.Mono;

public interface MongoVisitorOperations {
    Mono<Visitor> refreshVisitDate(String userId, String visitorUserId);
}
