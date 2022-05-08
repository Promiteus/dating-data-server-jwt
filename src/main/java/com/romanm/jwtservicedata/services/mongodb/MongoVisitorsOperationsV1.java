package com.romanm.jwtservicedata.services.mongodb;

import com.romanm.jwtservicedata.models.Visitor;
import com.romanm.jwtservicedata.services.interfaces.MongoVisitorOperations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
public class MongoVisitorsOperationsV1 implements MongoVisitorOperations {
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    /**
     * Обновит дату посещения или создаст новую запись посетителя, если его нет
     * @param userId String
     * @param visitorUserId String
     * @return Mono<Visitor>
     */
    @Override
    public Mono<Visitor> refreshVisitDate(String userId, String visitorUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Visitor.getVisitorUserIdFieldName()).is(userId));
        query.addCriteria(Criteria.where(Visitor.getVisitorVisitorUserIdFieldName()).is(visitorUserId));
        query.with(Sort.by(Visitor.getVisitorTimestampFieldName()).descending());
        Mono<Visitor> visitorMono = this.reactiveMongoTemplate.findOne(query, Visitor.class);

        Visitor newVisitor = new Visitor(userId, visitorUserId);;

        return visitorMono.flatMap(visitor -> {
            visitor.setTimestamp(LocalDateTime.now());
            return this.reactiveMongoTemplate.save(visitor);
        }).switchIfEmpty(this.reactiveMongoTemplate.save(newVisitor));
    }
}
