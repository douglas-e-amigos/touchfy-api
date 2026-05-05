package com.ufrn.dct.bsi.touchfy.infrastructure.security;

import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;

public interface TokenService {
    public String generateToken(final Usuario usuario);

    public String getSubject(final String token);
}
