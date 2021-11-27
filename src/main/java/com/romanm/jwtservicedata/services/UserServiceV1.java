package com.romanm.jwtservicedata.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanm.jwtservicedata.models.auth.AuthUser;
import com.romanm.jwtservicedata.repositories.RedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceV1 {

    private final RedisRepository redisRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceV1(RedisRepository redisRepository, BCryptPasswordEncoder passwordEncoder) {
        this.redisRepository = redisRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthUser readUserByUsername (String username) {
        Object res = this.redisRepository.find(username);
        AuthUser authUser = null;
        try {
            if (res != null) {
                ObjectMapper mapper = new ObjectMapper();
                authUser = mapper.readValue(res.toString(), AuthUser.class);
            }
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        return authUser;
     }


}
