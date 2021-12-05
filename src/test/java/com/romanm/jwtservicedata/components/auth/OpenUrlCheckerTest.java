package com.romanm.jwtservicedata.components.auth;

import com.romanm.jwtservicedata.constants.Api;
import com.romanm.jwtservicedata.constants.MessageConstants;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
public class OpenUrlCheckerTest {

    @Test
    public void checkTest() {
        OpenUrlChecker openUrlChecker = new OpenUrlChecker();
        boolean urlTestTrue = openUrlChecker.check(Api.API_PREFIX+Api.API_USER_IMAGE);
        boolean urlTestFalse = openUrlChecker.check(Api.API_PREFIX+Api.API_USER_IMAGES);

        log.info(MessageConstants.prefixMsg("urlTestTrue: "+urlTestTrue));
        log.info(MessageConstants.prefixMsg("urlTestFalse: "+urlTestFalse));

        Assert.assertTrue(urlTestTrue);
        Assert.assertFalse(urlTestFalse);
    }
}