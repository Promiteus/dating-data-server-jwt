package com.romanm.jwtservicedata.services;

import com.romanm.jwtservicedata.models.Visitor;
import com.romanm.jwtservicedata.repositories.pageble.VisitorPagebleRepository;
import com.romanm.jwtservicedata.services.interfaces.MongoVisitorOperations;
import com.romanm.jwtservicedata.services.interfaces.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VisitorServiceV1 implements VisitorService {

    @Qualifier("visitorPagebleRepository")
    private final VisitorPagebleRepository visitorPagebleRepository;
    private final MongoVisitorOperations mongoVisitorOperations;

    @Autowired
    public VisitorServiceV1(VisitorPagebleRepository visitorPagebleRepository, MongoVisitorOperations mongoVisitorOperations) {
        this.visitorPagebleRepository = visitorPagebleRepository;
        this.mongoVisitorOperations = mongoVisitorOperations;
    }

    /**
     * Показать постранично всех посетителей пользователя с userId
     * @param userId String
     * @param page int
     * @param pageSize int
     * @return Flux<Visitor>
     */
    @Override
    public Flux<Visitor> findPagebleVisitorsByUserId(String userId, int page, int pageSize) {
        if (userId.isBlank()) {
            return Flux.empty();
        }
        return this.visitorPagebleRepository.findVisitorByUserIdOrderByTimestampDesc(userId, PageRequest.of(page, pageSize > 0 ? pageSize : 10));
    }

    /**
     * Обновить дату посещения пользователя userId пользователем visitorUserId
     * @param userId String
     * @param visitorUserId String
     * @return Mono<Visitor>
     */
    @Override
    public Mono<Visitor> updateVisitor(String userId, String visitorUserId) {
        if (userId.isBlank() || visitorUserId.isBlank()) {
            return Mono.empty();
        }
        return this.mongoVisitorOperations.refreshVisitDate(userId, visitorUserId);
    }


}
