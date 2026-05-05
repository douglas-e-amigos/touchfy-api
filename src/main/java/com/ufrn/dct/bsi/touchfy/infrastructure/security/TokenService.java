package com.ufrn.dct.bsi.touchfy.infrastructure.security;

import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;

public interface TokenService {
    String generateToken(final Usuario usuario);

    String getSubject(final String token);
}
