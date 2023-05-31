package com.julyseven.jaeho.redis;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRepository {

    private final ValueOperations<String, String> operations;

    public RedisRepository(RedisTemplate<String, String> redisTemplate) {
        
        this.operations = redisTemplate.opsForValue();
    }


    public String set(String key, String value, Long expires) {

        this.operations.set(key, value, Duration.ofSeconds(expires));
        return value;
    }

    public Optional<String> get(String key) {

        return Optional.ofNullable(this.operations.get(key));
    }

    public void delete(String key) {

        this.operations.getAndDelete(key);
    }

}