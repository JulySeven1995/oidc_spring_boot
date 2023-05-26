package com.julyseven.jaeho.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.client.RestTemplate;


@Slf4j
public class JwtTokenValidator implements OAuth2TokenValidator<Jwt> {

    private final String userInfoEndpoint;

    private final OAuth2Error error;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public JwtTokenValidator(String userInfoEndpoint) {
        
        this.userInfoEndpoint = userInfoEndpoint;
        this.error = new OAuth2Error("invalid_token", "User information fetching failed", userInfoEndpoint);
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {

        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + token.getTokenValue());

        try {

            // 유저 정보 Fetching 을 통해 token 유효성 체크
            restTemplate.exchange(
                userInfoEndpoint,
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                Object.class
            );

            SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(token));

            return OAuth2TokenValidatorResult.success();

        } catch (Exception e) {
            log.error("Token Validation Failed. ->", e);
            return OAuth2TokenValidatorResult.failure(error);
        }
    }
}
