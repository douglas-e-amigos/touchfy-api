package com.ufrn.dct.bsi.touchfy.infrastructure.security;

import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SpringSecurityAuditorAwareImpl implements AuditorAware<UUID> {

    @Autowired
    @Lazy
    UsuarioRepository usuarioRepository;

    @Override
    public Optional<UUID> getCurrentAuditor() {
        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return Optional.empty();
        }

        return usuarioRepository.buscarPorNomeUsuario(authentication.getName())
                .map(Usuario::getId);
    }
}