package com.julyseven.jaeho.token.scheduler;

import com.julyseven.jaeho.token.repository.TokenRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class TokenWarder {
    
    protected final String userInfoEndpoint;

    protected final TokenRepository tokenRepository;

    abstract public void checkTokens();
}
