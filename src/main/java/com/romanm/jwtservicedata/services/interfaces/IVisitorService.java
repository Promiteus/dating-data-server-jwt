package com.romanm.jwtservicedata.services.interfaces;

import com.romanm.jwtservicedata.models.Visitor;
import reactor.core.publisher.Flux;

public interface IVisitorService {
    Flux<Visitor> findPagebleVisitorsByUserId(String userId, int page, int pageSize);
}
