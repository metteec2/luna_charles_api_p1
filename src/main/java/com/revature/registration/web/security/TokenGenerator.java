package com.revature.registration.web.security;

import com.revature.registration.web.dtos.Principal;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public class TokenGenerator {

    private JwtConfig jwtConfig;

    public TokenGenerator(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String createToken(Principal subject) {
        long now = System.currentTimeMillis();

        JwtBuilder tokenBuilder = Jwts.builder()
                                      .setId(subject.getId())
                                      .setSubject(subject.getEmail())
                                      .claim("role", subject.getRole())
                                      .setIssuer("revature")
                                      .setIssuedAt(new Date(now))
                                      .setExpiration(new Date(now + jwtConfig.getExpiration()))
                                      .signWith(jwtConfig.getSigAlg(), jwtConfig.getSigningKey());

        return jwtConfig.getPrefix() + tokenBuilder.compact();
    }

    public JwtConfig getJwtConfig() {
        return jwtConfig;
    }
}
