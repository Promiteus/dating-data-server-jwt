package com.romanm.jwtservicedata.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Repository
public class RedisRepository {
    private final String USER = "users";
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void add(String key, final Object object) {
        this.redisTemplate.opsForHash().put(USER, key, object);
    }

    public void delete(final String id) {
        this.redisTemplate.opsForHash().delete(USER, id);
    }

    public Object find(final String id){
        return this.redisTemplate.opsForHash().get(USER, id);
    }

    public Map<Object, Object> findAll(){
        return  this.redisTemplate.opsForHash().entries(USER);
    }
}
