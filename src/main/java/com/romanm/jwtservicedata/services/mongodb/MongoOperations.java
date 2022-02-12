package com.romanm.jwtservicedata.services.mongodb;

import com.romanm.jwtservicedata.constants.CommonConstants;
import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.ChatItem;
import com.romanm.jwtservicedata.models.UserProfile;
import com.romanm.jwtservicedata.models.Visitor;
import com.romanm.jwtservicedata.models.requests.SearchBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Slf4j
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

    /**
     * Получить список профилей постарнично c учетом параметров SearchBody, кроме профиля с notUserId
     * @param page int
     * @param pageSize int
     * @return Flux<UserProfile>
     */
    public Flux<UserProfile> findAllUserProfilesByPage(int page, int pageSize, String notUserId, SearchBody searchBody) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").ne(notUserId));

        if (searchBody.getKids() >= 0) {
            query.addCriteria(Criteria.where("kids").is(searchBody.getKids()));
        }
        if (searchBody.getAgeFrom() < searchBody.getAgeTo()) {
            query.addCriteria(Criteria.where("age").gte(searchBody.getAgeFrom()).lte(searchBody.getAgeTo()));
        }

        Optional.ofNullable(searchBody.getFamilyStatus()).ifPresent(familyStatus -> {
            query.addCriteria(Criteria.where("familyStatus").is(familyStatus));
        });
        Optional.ofNullable(searchBody.getMeetPreferences()).ifPresent(meetPreferences -> {
            query.addCriteria(Criteria.where("meetPreferences").is(meetPreferences));
        });
        Optional.ofNullable(searchBody.getSexOrientation()).ifPresent(sexOrientation -> {
            query.addCriteria(Criteria.where("sexOrientation").is(sexOrientation));
        });
        Optional.ofNullable(searchBody.getSex()).ifPresent(sex -> {
            query.addCriteria(Criteria.where("sex").is(sex));
        });
        Optional.ofNullable(searchBody.getCountry()).ifPresent(country -> {
            query.addCriteria(Criteria.where("country").is(country));
        });
        Optional.ofNullable(searchBody.getRegion()).ifPresent(region -> {
            query.addCriteria(Criteria.where("region").is(region));
        });
        Optional.ofNullable(searchBody.getLocality()).ifPresent(locality -> {
            query.addCriteria(Criteria.where("locality").is(locality));
        });

        query.with(PageRequest.of(page, pageSize));
        return reactiveMongoTemplate.find(query, UserProfile.class);
    }

    /**
     * Получить уникальные чаты по полю fromUserId. Вернет [201,202,203]
     * @param userId String
     * @param page int
     * @param pageSize int
     * @return Flux<ChatItem>
     */
    public Flux<String> findDistinctProfileIdOfChat(String userId, int page, int pageSize) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        query.with(Sort.by("timestamp").descending());
        return reactiveMongoTemplate.findDistinct(query, "fromUserId", ChatItem.class, String.class).skip(page*pageSize).take(pageSize);
    }

}
