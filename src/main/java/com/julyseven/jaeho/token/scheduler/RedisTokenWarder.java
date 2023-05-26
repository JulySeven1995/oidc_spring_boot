package com.julyseven.jaeho.token.scheduler;

import java.util.Set;

import com.julyseven.jaeho.token.scheduler.TokenWarder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.julyseven.jaeho.token.repository.RedisTokenRepository;
import com.julyseven.jaeho.token.repository.TokenRepository;

@Slf4j
@Component
@EnableScheduling
public class RedisTokenWarder extends TokenWarder {
    private final RestTemplate restTemplate = new RestTemplate();
    public RedisTokenWarder(ClientRegistrationRepository clientRegistrationRepository, RedisTokenRepository tokenRepository) {
        super(clientRegistrationRepository.findByRegistrationId("oidc").getProviderDetails().getUserInfoEndpoint().getUri(), tokenRepository);
    }

    @Scheduled(fixedDelay = 10000L)
    @Override
    public void checkTokens() {
        
        Set<String> tokens = tokenRepository.findAllTokens();

        for(String token: tokens) {
            
            HttpHeaders headers = new HttpHeaders();

            headers.set("Authorization", "Bearer " + token);

            try {
                // 유저 정보 Fetching 을 통해 token 유효성 체크
                restTemplate.exchange(
                    userInfoEndpoint,
                    HttpMethod.GET,
                    new HttpEntity<String>(headers),
                    Object.class
                );

                log.debug("토큰이 잘 살아 있어요!");
            } catch(Exception ignore) {
                log.debug("토큰이 삭제 됐어요!");
                tokenRepository.delete(token);
            }
        }
    }
}
