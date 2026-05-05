package com.ufrn.dct.bsi.touchfy.adapters.outbound.security;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioDetalhesImplTest {

    private UsuarioEntity criarUsuario() {
        return UsuarioEntity.builder()
                .id(UUID.randomUUID())
                .nome("Nome Teste")
                .nomeUsuario("usuario_teste")
                .senha("senha_hash")
                .email(new Email("teste@email.com"))
                .dataNascimento(LocalDate.now())
                .build();
    }

    @Test
    void deveRetornarUsernameCorretamente() {
        final UsuarioEntity usuario = criarUsuario();

        final UsuarioDetalhesImpl detalhes = UsuarioDetalhesImpl.builder()
                .usuario(usuario)
                .build();

        assertEquals("usuario_teste", detalhes.getUsername());
    }

    @Test
    void deveRetornarSenhaCorretamente() {
        final UsuarioEntity usuario = criarUsuario();

        final UsuarioDetalhesImpl detalhes = UsuarioDetalhesImpl.builder()
                .usuario(usuario)
                .build();

        assertEquals("senha_hash", detalhes.getPassword());
    }

    @Test
    void deveRetornarAuthoritiesVazia() {
        final UsuarioEntity usuario = criarUsuario();

        final UsuarioDetalhesImpl detalhes = UsuarioDetalhesImpl.builder()
                .usuario(usuario)
                .build();

        assertTrue(detalhes.getAuthorities().isEmpty());
    }

    @Test
    void deveRetornarContaValida() {
        final UsuarioEntity usuario = criarUsuario();

        final UsuarioDetalhesImpl detalhes = UsuarioDetalhesImpl.builder()
                .usuario(usuario)
                .build();

        assertTrue(detalhes.isAccountNonExpired());
        assertTrue(detalhes.isAccountNonLocked());
        assertTrue(detalhes.isCredentialsNonExpired());
        assertTrue(detalhes.isEnabled());
    }
}