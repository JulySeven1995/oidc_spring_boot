package com.julyseven.jaeho.token.repository;

import java.util.Optional;
import java.util.Set;

public interface TokenRepository {
    
    Optional<String> get(String key);

    void set(String key, String value, Long expires);

    Set<String> findAllTokens();

    void delete(String key);
}
