package com.julyseven.jaeho.config;

import com.julyseven.jaeho.oauth2.JwtTokenValidator;
import com.julyseven.jaeho.token.repository.RedisTokenRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfiguration {

    private final ClientRegistration clientRegistration;
    public SecurityConfiguration(ClientRegistrationRepository clientRegistrationRepository) {

        this.clientRegistration = clientRegistrationRepository.findByRegistrationId("oidc");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf()
            .disable()
            .authorizeHttpRequests()
            .mvcMatchers("/api/**").authenticated()
        .and()
            .oauth2ResourceServer()
            .jwt()
            .jwtAuthenticationConverter(this.jwtAuthenticationConverter());

        return http.build();
    }


    @Bean
    public JwtDecoder jwtDecoder(RedisTokenRepository tokenService) {

        NimbusJwtDecoder jwtDecoder = JwtDecoders.fromIssuerLocation(clientRegistration.getProviderDetails().getIssuerUri());

        OAuth2TokenValidator<Jwt> validator = new JwtTokenValidator(
            clientRegistration.getProviderDetails().getUserInfoEndpoint().getUri(),
            tokenService
        );

        jwtDecoder.setJwtValidator(validator);

        return jwtDecoder;
    }

    private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(new JwtGrantedAuthoritiesConverter());

        return converter;
    }

}
