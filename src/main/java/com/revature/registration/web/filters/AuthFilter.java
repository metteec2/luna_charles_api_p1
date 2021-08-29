package com.revature.registration.web.filters;

import com.revature.registration.web.dtos.Principal;
import com.revature.registration.web.security.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter extends HttpFilter {

    private final JwtConfig jwtConfig;

    public AuthFilter(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        parseToken(req);
        chain.doFilter(req,resp);
    }

    public void parseToken(HttpServletRequest req) {

        try {
            String header = req.getHeader(jwtConfig.getHeader());

            if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
                // use logger here to warn users
                return;
            }

            String token = header.replaceAll(jwtConfig.getPrefix(), "");

            Claims jwtClaims = Jwts.parser()
                                   .setSigningKey(jwtConfig.getSigningKey())
                                   .parseClaimsJws(token)
                                   .getBody();

            req.setAttribute("principal", new Principal(jwtClaims));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
