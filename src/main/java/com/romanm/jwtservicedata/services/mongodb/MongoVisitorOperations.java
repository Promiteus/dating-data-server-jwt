package com.romanm.jwtservicedata.services.mongodb;

import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.models.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class MongoVisitorOperations {
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    public Flux<Visitor> findVisitorByUserIdDistinctVisitorUserIdOrderByTimestampDesc(String userId, int page, int pageSize) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Visitor.getVisitorUserIdFieldName()).is(userId));
        query.with(Sort.by(Visitor.getVisitorTimestampFieldName()).descending());
        query.with(PageRequest.of(page, pageSize));
        return reactiveMongoTemplate.find(query, Visitor.class).distinct(Visitor::getVisitorUserId);//.limitRate(pageSize);
    }


}
