package com.romanm.jwtservicedata.services.mongodb;

import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.models.UserProfile;
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
public class MongoOperations {
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    /**
     * Постраничный запрос уникальных посетителей, отсортированный по последней дате посещения
     * @param userId String
     * @param page int
     * @param pageSize int
     * @return Flux<Visitor>
     */
    public Flux<Visitor> findVisitorByUserIdDistinctVisitorUserIdOrderByTimestampDesc(String userId, int page, int pageSize) {
        Query query = new Query();
        query.addCriteria(Criteria.where(Visitor.getVisitorUserIdFieldName()).is(userId));
        query.with(Sort.by(Visitor.getVisitorTimestampFieldName()).descending());
        query.with(PageRequest.of(page, pageSize));
        return reactiveMongoTemplate.find(query, Visitor.class).distinct(Visitor::getVisitorUserId);//.limitRate(pageSize);
    }

    /**
     * Получить список профилей постарнично, кроме профиля с notUserId
     * @param page int
     * @param pageSize int
     * @return Flux<UserProfile>
     */
    public Flux<UserProfile> findAllUserProfilesByPage(int page, int pageSize, String notUserId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").ne(notUserId));
        query.with(PageRequest.of(page, pageSize));
        return reactiveMongoTemplate.find(query, UserProfile.class);
    }

}
