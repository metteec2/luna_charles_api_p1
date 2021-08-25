package com.revature.registration.web.security;

import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.util.Properties;

public class JwtConfig {

    private String header;
    private String prefix;
    private String secret;
    private int expiration;
    private final SignatureAlgorithm sigAlg = SignatureAlgorithm.HS256;
    private Key signingKey;

    public JwtConfig() {
        try {
            Properties appProperties = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            appProperties.load(loader.getResourceAsStream("app.properties"));

            this.header = appProperties.getProperty("jwt.header");
            this.prefix = appProperties.getProperty("jwt.prefix");
            this.secret = appProperties.getProperty("jwt.secret");
            this.expiration = Integer.parseInt(appProperties.getProperty("jwt.expiration"));

            byte[] secretBytes = DatatypeConverter.parseBase64Binary(this.secret);
            signingKey = new SecretKeySpec(secretBytes,sigAlg.getJcaName());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public String getHeader() {
        return header;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSecret() {
        return secret;
    }

    public int getExpiration() {
        return expiration;
    }

    public SignatureAlgorithm getSigAlg() {
        return sigAlg;
    }

    public Key getSigningKey() {
        return signingKey;
    }
}
