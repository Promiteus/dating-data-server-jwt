package com.romanm.jwtservicedata.repositories.pageble;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RunWith(SpringRunner.class)
@ActiveProfiles(value = {"test"})
public class ChatMessagePageRepositoryTest {
    @Autowired
    private ChatMessagePageRepository chatMessagePageRepository;

    @Test
    public void chatMessagePageTest() {

    }
}