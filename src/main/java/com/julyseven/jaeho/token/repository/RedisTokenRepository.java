package com.julyseven.jaeho.token.repository;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.util.Strings;
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

    @Override
    public void delete(String key) {
        this.redisRepository.delete(namespace + key);
    }
}
