package com.julyseven.jaeho.token;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.julyseven.jaeho.redis.RedisRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RedisTokenRepository implements TokenRepository {
    
    private final String namespace = "token:";

    private final RedisRepository redisRepository;

    public Optional<String> get(String key) {

        return this.redisRepository.get(namespace + key);
    }

    public void set(String key, String value, Long expires) {

        this.redisRepository.set(namespace + key, value, expires);
    }
}
