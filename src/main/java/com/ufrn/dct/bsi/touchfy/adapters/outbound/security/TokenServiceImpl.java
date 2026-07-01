package com.ufrn.dct.bsi.touchfy.adapters.outbound.security;

import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.infrastructure.security.TokenService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

  private final long accessExpiration;
  private final long refreshExpiration;

  private final SecretKey accessKey;
  private final SecretKey refreshKey;

  public TokenServiceImpl(
      @Value("${jwt.access.secret}") final String accessSecret,
      @Value("${jwt.refresh.secret}") final String refreshSecret,
      @Value("${jwt.access.expiration}") final long accessExpiration,
      @Value("${jwt.refresh.expiration}") final long refreshExpiration) {
    this.accessExpiration = accessExpiration;
    this.refreshExpiration = refreshExpiration;

    this.accessKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(accessSecret));

    this.refreshKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(refreshSecret));
  }

  @Override
  public String generateAccessToken(final Usuario usuario) {
    return generateToken(usuario, accessExpiration, accessKey);
  }

  @Override
  public String generateRefreshToken(final Usuario usuario) {
    return generateToken(usuario, refreshExpiration, refreshKey);
  }

  private String generateToken(final Usuario usuario, final long expiration, final SecretKey key) {
    final Date now = new Date();

    return Jwts.builder()
        .subject(usuario.getNomeUsuario())
        .issuer("touchfy-api")
        .issuedAt(now)
        .expiration(new Date(now.getTime() + expiration))
        .id(UUID.randomUUID().toString())
        .signWith(key)
        .compact();
  }

  private String getSubjectWithScret(final SecretKey secretKey, final String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  @Override
  public String getSubject(final String token) {
    return getSubjectWithScret(accessKey, token);
  }

  @Override
  public String getRefreshSubject(final String token) {
    return getSubjectWithScret(refreshKey, token);
  }

  private boolean isValid(final SecretKey secretKey, final String token) {
    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);

      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  @Override
  public boolean isTokenValid(final String token) {
    return isValid(accessKey, token);
  }

  @Override
  public boolean isRefreshTokenValid(final String token) {
    return isValid(refreshKey, token);
  }
}
