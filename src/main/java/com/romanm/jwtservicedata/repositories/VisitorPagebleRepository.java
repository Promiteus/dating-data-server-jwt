package com.romanm.jwtservicedata.repositories;

import com.romanm.jwtservicedata.models.Visitor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository("visitorPagebleRepository")
public interface VisitorPagebleRepository extends ReactiveSortingRepository<Visitor, String> {
    Flux<Visitor> findVisitorByUserIdOrderByTimestampDesc(String userId, Pageable page);
    Mono<Void> removeByUserIdIn(List<String> userIds);
}
