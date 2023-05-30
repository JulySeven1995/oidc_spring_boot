package com.julyseven.jaeho.rest;

import com.julyseven.jaeho.token.repository.TokenRepository;
import com.nimbusds.jwt.JWTParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/sso")
@RequiredArgsConstructor
public class LogoutResource {

    private final TokenRepository tokenRepository;

    @PostMapping(path = "/logout", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public void logout(@RequestParam("logout_token") String logoutToken) throws ParseException {

        String sid = String.valueOf(JWTParser.parse(logoutToken).getJWTClaimsSet().getClaim("sid"));
        tokenRepository.delete(sid);
    }
}
