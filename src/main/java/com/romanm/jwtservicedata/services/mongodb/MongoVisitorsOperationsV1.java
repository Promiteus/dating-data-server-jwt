package com.romanm.jwtservicedata.services.mongodb;

import com.romanm.jwtservicedata.models.Visitor;
import com.romanm.jwtservicedata.services.interfaces.MongoVisitorOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class MongoVisitorsOperationsV1 implements MongoVisitorOperations {
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Mono<Visitor> refreshVisitDate(String userId, String visitorUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Visitor.getVisitorUserIdFieldName()).is(userId));
        query.addCriteria(Criteria.where(Visitor.getVisitorVisitorUserIdFieldName()).is(visitorUserId));
        query.with(Sort.by(Visitor.getVisitorTimestampFieldName()).descending());
        Mono<Visitor> visitorMono = this.reactiveMongoTemplate.findOne(query, Visitor.class);

        return visitorMono.flatMap(visitor -> {
            Visitor refreshedVisitor = visitor;
            if (visitor == null) {
                refreshedVisitor = new Visitor(userId, visitorUserId);
            }
            refreshedVisitor.setTimestamp(LocalDateTime.now());
            return this.reactiveMongoTemplate.save(refreshedVisitor);
        });
    }
}
