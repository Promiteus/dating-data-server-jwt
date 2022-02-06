package com.romanm.jwtservicedata.services.mongodb;

import com.romanm.jwtservicedata.models.ChatItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.util.List;

import static org.junit.Assert.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@RunWith(SpringRunner.class)
public class MongoOperationsTest {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    /**
     * Получить уникальные чаты по полю fromUserId. Вернет [201,202,203]
     * @param userId String
     * @return Flux<ChatItem>
     */
    private Flux<String> findDistinctProfileIdOfChat(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return reactiveMongoTemplate.findDistinct(query, "fromUserId", ChatItem.class, String.class);
    }

    /**
     * Получить уникальные чаты по полю fromUserId. Вернет [201,202,203] постранично
     * @param userId String
     * @param page int
     * @param pageSize int
     * @return Flux<String>
     */
    private Flux<String> findDistinctProfileIdOfChatPageable(String userId, int page, int pageSize) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        return reactiveMongoTemplate.findDistinct(query, "fromUserId", ChatItem.class, String.class).skip((long) page *pageSize).take(pageSize);
    }

    @Test
    public void findDistinctProfileIdOfChatPageable() {
        List<String> ids = this.findDistinctProfileIdOfChat("200").collectList().block();
        log.info("Source list userIds: "+ids.toString());

        int page = 0;
        while (page < 10) {
            log.info("UserIds page "+page+": "+this.findDistinctProfileIdOfChatPageable("200", page, 10).collectList().block());
            page++;
        }
    }

    @Test
    public void findDistinctProfileIdOfChat() {
        long startMs;

        startMs = System.currentTimeMillis();
        List<String> ids = this.findDistinctProfileIdOfChat("200").collectList().block();
        log.info("Source list userIds: "+ids.toString());
        log.info("Time execution (skipped and limit): "+String.format("%d, ms: ", (System.currentTimeMillis() - startMs)));

        startMs = System.currentTimeMillis();
        List<String> skipIds = this.findDistinctProfileIdOfChat("200").skip(10).collectList().block();
        log.info("Source skipped list userIds: "+skipIds.toString());
        log.info("Time execution (skipped and limit): "+String.format("%d, ms: ", (System.currentTimeMillis() - startMs)));

        startMs = System.currentTimeMillis();
        List<String> skipAndLimitIds = this.findDistinctProfileIdOfChat("200").skip(10).take(5).collectList().block();
        log.info("Source skipped and limit list userIds: "+skipAndLimitIds.toString());
        log.info("Time execution (skipped and limit): "+String.format("%d, ms: ", (System.currentTimeMillis() - startMs)));
    }
}