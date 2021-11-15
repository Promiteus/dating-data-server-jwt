package com.romanm.jwtservicedata.repositories;

import com.romanm.jwtservicedata.models.Visitor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.awt.print.Pageable;

@Repository
public interface VisitorRepository extends ReactiveCrudRepository<Visitor, String> {
     Flux<Visitor> findVisitorByUserId(String userId);
}
