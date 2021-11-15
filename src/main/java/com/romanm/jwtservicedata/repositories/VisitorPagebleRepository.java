package com.romanm.jwtservicedata.repositories;

import com.romanm.jwtservicedata.models.Visitor;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

import java.awt.print.Pageable;

public interface VisitorPagebleRepository extends ReactiveSortingRepository<Visitor, String> {
    Flux<Visitor> findVisitorByUserId(String userId, Pageable page);
}
