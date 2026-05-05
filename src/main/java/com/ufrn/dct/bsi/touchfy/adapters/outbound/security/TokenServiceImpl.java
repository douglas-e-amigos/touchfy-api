package com.ufrn.dct.bsi.touchfy.adapters.outbound.security;

import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.infrastructure.security.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${jwt.expiration}")
    private final long expiration;

    private final SecretKey key;

    public TokenServiceImpl(
            @Value("${jwt.secret}") final String secret,
            @Value("${jwt.expiration}") final long expiration
    ) {
        this.expiration = expiration;
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Override
    public String generateToken(final Usuario usuario) {
        return Jwts.builder()
                .subject(usuario.getId().toString())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(key)
                .compact();
    }

    @Override
    public String getSubject(final String token) {
        return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
    }
}
