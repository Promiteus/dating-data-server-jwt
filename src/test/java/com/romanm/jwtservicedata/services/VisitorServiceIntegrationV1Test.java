package com.romanm.jwtservicedata.services;

import com.romanm.jwtservicedata.constants.MessageConstants;
import com.romanm.jwtservicedata.models.Visitor;
import com.romanm.jwtservicedata.repositories.VisitorPagebleRepository;
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

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles(value = {"test"})
public class VisitorServiceIntegrationV1Test {
    @Autowired
    private VisitorPagebleRepository visitorPagebleRepository;

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
        for (int i = 0; i < 3; i++) {
            visitorList.add(this.visitorPagebleRepository.save(new Visitor("200", "201")).delayElement(Duration.ofMillis(500)).block());
        }

        Flux<Visitor> visitorFlux = this.visitorPagebleRepository.findVisitorByUserIdOrderByTimestampDesc("200", PageRequest.of(0, 4));

        log.info(MessageConstants.prefixMsg("Got visitors: "+visitorList));
        try {
            Thread.sleep(6000);


            visitorList.forEach(visitor -> {
                this.visitorPagebleRepository.delete(visitor).subscribe();
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}