package com.julyseven.jaeho.token.repository;

import java.util.Optional;

public interface TokenRepository {
    
    Optional<String> get(String key);

    void set(String key, String value, Long expires);

    void delete(String key);
}
