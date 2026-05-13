package com.ufrn.dct.bsi.touchfy.infrastructure.security;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.security.UsuarioDetalhesImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SpringSecurityAuditorAwareImplTest {

    private final SpringSecurityAuditorAwareImpl auditorAware = new SpringSecurityAuditorAwareImpl();

    @AfterEach
    void limparContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void deveRetornarIdDoPrincipalAutenticado() {
        final UUID usuarioId = UUID.randomUUID();
        final UsuarioEntity usuarioEntity = new UsuarioEntity();
        usuarioEntity.setId(usuarioId);
        usuarioEntity.setNomeUsuario("usuario-teste");

        final UsuarioDetalhesImpl principal = UsuarioDetalhesImpl.builder()
                .usuario(usuarioEntity)
                .build();

        final var authentication = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertEquals(usuarioId, auditorAware.getCurrentAuditor().orElseThrow());
    }

    @Test
    void deveRetornarVazioQuandoPrincipalNaoForUsuarioDetalhes() {
        final var authentication = new UsernamePasswordAuthenticationToken("usuario", null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        assertTrue(auditorAware.getCurrentAuditor().isEmpty());
    }
}