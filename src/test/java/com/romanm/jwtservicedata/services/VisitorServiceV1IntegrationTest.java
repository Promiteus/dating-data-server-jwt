package com.romanm.jwtservicedata.services;

import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.Visitor;
import com.romanm.jwtservicedata.repositories.VisitorPagebleRepository;
import com.romanm.jwtservicedata.services.mongodb.MongoOperations;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles(value = {"test"})
public class VisitorServiceV1IntegrationTest {
    @Autowired
    private VisitorPagebleRepository visitorPagebleRepository;
    @Autowired
    private MongoOperations mongoVisitorOperations;

    @Test
    public void findPagebleVisitorsByUserId() {
        Flux<Visitor> visitorFlux = this.visitorPagebleRepository.findVisitorByUserIdOrderByTimestampDesc("200", PageRequest.of(0, 2));

        List<Visitor> visitorList = visitorFlux.collectList().block();
        log.info(MessageConstants.prefixMsg("Got visitors count: "+visitorList.size()));
        Assert.assertEquals(2, visitorList.size());

        log.info(MessageConstants.prefixMsg("Got visitors: "+visitorList));
    }

    @Test
    public void findDistinctVisitorsByUserId() {
        List<Visitor> visitorList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            visitorList.add(new Visitor("200", "201"));
        }
        this.visitorPagebleRepository.saveAll(visitorList).delayElements(Duration.ofMillis(500)).publish().connect();

        List<Visitor> visitors = this.mongoVisitorOperations.findVisitorByUserIdDistinctVisitorUserIdOrderByTimestampDesc("200", 0, 30).collectList().block();
        log.info(MessageConstants.prefixMsg("Got visitors count: "+visitors.size()));
        log.info(MessageConstants.prefixMsg("Got visitors: "+visitors));

        if (visitorList.size() > 0) {
            this.visitorPagebleRepository.deleteAll(visitorList).block();
        }

    }
}