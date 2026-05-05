package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.UsuarioJpaRepository;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.AtualizarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioRepositoryImplTest {

    private UsuarioJpaRepository jpaRepository;
    private UsuarioMapper usuarioMapper;

    private UsuarioRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(UsuarioJpaRepository.class);
        usuarioMapper = mock(UsuarioMapper.class);

        repository = new UsuarioRepositoryImpl(jpaRepository, usuarioMapper);
    }

    private Usuario criarUsuarioValido() {
        return new Usuario(
                UUID.randomUUID(),
                "Nome",
                "username",
                "senha",
                new Email("teste@email.com"),
                null,
                false,
                new Date()
        );
    }

    @Test
    void deveSalvarUsuarioChamandoMapperEJpa() {
        Usuario usuario = criarUsuarioValido();
        UsuarioEntity entity = new UsuarioEntity();

        when(usuarioMapper.toEntity(usuario)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);

        UsuarioEntity resultado = repository.salvar(usuario);

        assertNotNull(resultado);
        assertEquals(entity, resultado);

        verify(usuarioMapper, times(1)).toEntity(usuario);
        verify(jpaRepository, times(1)).save(entity);
    }

    @Test
    void devePropagarRetornoDoJpaRepository() {
        Usuario usuario = criarUsuarioValido();

        UsuarioEntity entityEntrada = new UsuarioEntity();
        UsuarioEntity entitySalva = new UsuarioEntity();

        when(usuarioMapper.toEntity(usuario)).thenReturn(entityEntrada);
        when(jpaRepository.save(entityEntrada)).thenReturn(entitySalva);

        UsuarioEntity resultado = repository.salvar(usuario);

        assertEquals(entitySalva, resultado);
    }

    @Test
    void deveChamarMapperAntesDeSalvar() {
        Usuario usuario = criarUsuarioValido();
        UsuarioEntity entity = new UsuarioEntity();

        when(usuarioMapper.toEntity(usuario)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);

        repository.salvar(usuario);

        var inOrder = inOrder(usuarioMapper, jpaRepository);

        inOrder.verify(usuarioMapper).toEntity(usuario);
        inOrder.verify(jpaRepository).save(entity);
    }

    @Test
    void deveRetornarUsuarioQuandoEncontrado() {
        final String nomeUsuario = "usuario_teste";
        final UsuarioEntity entity = new UsuarioEntity();

        when(jpaRepository.findByNomeUsuario(nomeUsuario))
                .thenReturn(Optional.of(entity));

        final Optional<UsuarioEntity> resultado =
                repository.acharPeloNomeDeUsuario(nomeUsuario);

        assertTrue(resultado.isPresent());
        assertEquals(entity, resultado.get());

        verify(jpaRepository, times(1))
                .findByNomeUsuario(nomeUsuario);
    }

    @Test
    void deveRetornarOptionalVazioQuandoNaoEncontrado() {
        final String nomeUsuario = "inexistente";

        when(jpaRepository.findByNomeUsuario(nomeUsuario))
                .thenReturn(Optional.empty());

        final Optional<UsuarioEntity> resultado =
                repository.acharPeloNomeDeUsuario(nomeUsuario);

        assertTrue(resultado.isEmpty());

        verify(jpaRepository, times(1))
                .findByNomeUsuario(nomeUsuario);
    }

    @Test
    void deveDelegarChamadaParaJpaRepository() {
        final String nomeUsuario = "usuario_teste";

        when(jpaRepository.findByNomeUsuario(nomeUsuario))
                .thenReturn(Optional.empty());

        repository.acharPeloNomeDeUsuario(nomeUsuario);

        verify(jpaRepository, only())
                .findByNomeUsuario(nomeUsuario);
    }

    @Test
    void deveAtualizarUsuarioParcialmenteQuandoEncontrado() {
        final var id = UUID.randomUUID();
        final var request = new AtualizarUsuarioRequest(
                "Novo Nome",
                "novo_username",
                LocalDate.of(2000, 1, 1)
        );

        final var entity = new UsuarioEntity();

        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(jpaRepository.save(entity)).thenReturn(entity);

        repository.atualizarUsuarioParcialmente(id, request);

        verify(jpaRepository).findById(id);
        verify(usuarioMapper).updateEntity(request, entity);
        verify(jpaRepository).save(entity);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontradoParaAtualizacao() {
        final var id = UUID.randomUUID();
        final var request = new AtualizarUsuarioRequest(
                "Novo Nome",
                "novo_username",
                LocalDate.of(2000, 1, 1)
        );

        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> repository.atualizarUsuarioParcialmente(id, request)
        );

        assertEquals("Usuário não encontrado.", exception.getMessage());

        verify(jpaRepository).findById(id);
        verifyNoInteractions(usuarioMapper);
        verify(jpaRepository, never()).save(any());
    }

    @Test
    void deveChamarMapperAntesDeSalvarNaAtualizacaoParcial() {
        final var id = UUID.randomUUID();
        final var request = new AtualizarUsuarioRequest(
                "Novo Nome",
                "novo_username",
                LocalDate.of(2000, 1, 1)
        );

        final var entity = new UsuarioEntity();

        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(jpaRepository.save(entity)).thenReturn(entity);

        repository.atualizarUsuarioParcialmente(id, request);

        final var inOrder = inOrder(jpaRepository, usuarioMapper);

        inOrder.verify(jpaRepository).findById(id);
        inOrder.verify(usuarioMapper).updateEntity(request, entity);
        inOrder.verify(jpaRepository).save(entity);
    }
}