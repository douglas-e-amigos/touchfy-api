package com.ufrn.dct.bsi.touchfy.infrastructure.security;

import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;

public interface TokenService {
  String generateAccessToken(final Usuario usuario);

  String generateRefreshToken(final Usuario usuario);

  String getSubject(final String token);

  String getRefreshSubject(final String token);

  boolean isTokenValid(final String token);

  boolean isRefreshTokenValid(final String token);
}
