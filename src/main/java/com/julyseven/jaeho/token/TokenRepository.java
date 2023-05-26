package com.julyseven.jaeho.token;

import java.util.Optional;

public interface TokenRepository {
    
    Optional<String> get(String key);

    void set(String key, String value, Long expires);
}
