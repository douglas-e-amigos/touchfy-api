package com.ufrn.dct.bsi.touchfy.adapters.outbound.security;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioDetalhesServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioMapper usuarioMapper;

    private UsuarioDetalhesServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        service = new UsuarioDetalhesServiceImpl(usuarioRepository, usuarioMapper);
    }

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
    void deveRetornarUserDetailsQuandoUsuarioExiste() {
        final String username = "usuario_teste";
        final UsuarioEntity usuario = criarUsuario();

        when(usuarioRepository.acharPeloNomeDeUsuario(username))
                .thenReturn(Optional.of(usuario));

        final UserDetails resultado = service.loadUserByUsername(username);

        assertNotNull(resultado);
        assertEquals(username, resultado.getUsername());
        assertEquals("senha_hash", resultado.getPassword());

        verify(usuarioRepository).acharPeloNomeDeUsuario(username);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoExiste() {
        final String username = "inexistente";

        when(usuarioRepository.acharPeloNomeDeUsuario(username))
                .thenReturn(Optional.empty());

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.loadUserByUsername(username)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());

        verify(usuarioRepository).acharPeloNomeDeUsuario(username);
    }
}
