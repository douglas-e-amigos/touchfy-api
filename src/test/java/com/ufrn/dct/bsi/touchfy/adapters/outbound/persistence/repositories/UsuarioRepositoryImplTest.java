package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.RoleEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.RoleJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.UsuarioJpaRepository;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.AtualizarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import org.springframework.data.domain.AuditorAware;
import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UsuarioRepositoryImplTest {

    private UsuarioJpaRepository jpaRepository;
    private UsuarioMapper usuarioMapper;
    private AuditorAware<UUID> auditorAware;

    private UsuarioRepositoryImpl repository;
    private RoleJpaRepository roleJpaRepository;

    @BeforeEach
    void setUp() {
        jpaRepository = mock(UsuarioJpaRepository.class);
        usuarioMapper = mock(UsuarioMapper.class);
        auditorAware = mock(AuditorAware.class);
        roleJpaRepository = mock(RoleJpaRepository.class);

        repository = new UsuarioRepositoryImpl(jpaRepository, usuarioMapper, auditorAware, roleJpaRepository);
    }

    private Usuario criarUsuarioValido() {
        return new Usuario(UUID.randomUUID(), "Nome", "username", "senha", new Email("teste@email.com"), null, false, LocalDate.now(), Set.of());
    }

    @Test
    void deveSalvarUsuarioChamandoMapperEJpa() {
        Usuario usuario = criarUsuarioValido();
        UsuarioEntity entity = new UsuarioEntity();

        when(usuarioMapper.toEntity(usuario)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(ERole.OUVINTE);

        when(roleJpaRepository.findByName(ERole.OUVINTE))
                .thenReturn(Optional.of(roleEntity));

        UsuarioEntity resultado = repository.salvar(usuario, ERole.OUVINTE);

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

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(ERole.OUVINTE);

        when(roleJpaRepository.findByName(ERole.OUVINTE))
                .thenReturn(Optional.of(roleEntity));

        UsuarioEntity resultado = repository.salvar(usuario, ERole.OUVINTE);

        assertEquals(entitySalva, resultado);
    }

    @Test
    void deveChamarMapperAntesDeSalvar() {
        Usuario usuario = criarUsuarioValido();
        UsuarioEntity entity = new UsuarioEntity();

        when(usuarioMapper.toEntity(usuario)).thenReturn(entity);
        when(jpaRepository.save(entity)).thenReturn(entity);

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(ERole.OUVINTE);

        when(roleJpaRepository.findByName(ERole.OUVINTE))
                .thenReturn(Optional.of(roleEntity));

        repository.salvar(usuario, ERole.OUVINTE);

        var inOrder = inOrder(usuarioMapper, jpaRepository);

        inOrder.verify(usuarioMapper).toEntity(usuario);
        inOrder.verify(jpaRepository).save(entity);
    }

    @Test
    void deveRetornarUsuarioQuandoEncontrado() {
        final String nomeUsuario = "usuario_teste";
        final UsuarioEntity entity = new UsuarioEntity();

        when(jpaRepository.findByNomeUsuario(nomeUsuario)).thenReturn(Optional.of(entity));

        final Optional<UsuarioEntity> resultado = repository.acharPeloNomeDeUsuario(nomeUsuario);

        assertTrue(resultado.isPresent());
        assertEquals(entity, resultado.get());

        verify(jpaRepository, times(1)).findByNomeUsuario(nomeUsuario);
    }

    @Test
    void deveRetornarOptionalVazioQuandoNaoEncontrado() {
        final String nomeUsuario = "inexistente";

        when(jpaRepository.findByNomeUsuario(nomeUsuario)).thenReturn(Optional.empty());

        final Optional<UsuarioEntity> resultado = repository.acharPeloNomeDeUsuario(nomeUsuario);

        assertTrue(resultado.isEmpty());

        verify(jpaRepository, times(1)).findByNomeUsuario(nomeUsuario);
    }

    @Test
    void deveDelegarChamadaParaJpaRepository() {
        final String nomeUsuario = "usuario_teste";

        when(jpaRepository.findByNomeUsuario(nomeUsuario)).thenReturn(Optional.empty());

        repository.acharPeloNomeDeUsuario(nomeUsuario);

        verify(jpaRepository, only()).findByNomeUsuario(nomeUsuario);
    }

    @Test
    void deveAtualizarUsuarioParcialmenteQuandoEncontrado() {
        final var id = UUID.randomUUID();
        final var request = AtualizarUsuarioRequest.builder()
                .nome("Novo Nome")
                .nomeUsuario("novo_username")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .build();

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
        final var request = AtualizarUsuarioRequest.builder()
                .nome("Novo Nome")
                .nomeUsuario("novo_username")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .build();

        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        final RuntimeException exception = assertThrows(
                RuntimeException.class, () -> repository.atualizarUsuarioParcialmente(id, request)
        );

        assertEquals("Usuário não encontrado.", exception.getMessage());

        verify(jpaRepository).findById(id);
        verifyNoInteractions(usuarioMapper);
        verify(jpaRepository, never()).save(any());
    }

    @Test
    void deveChamarMapperAntesDeSalvarNaAtualizacaoParcial() {
        final var id = UUID.randomUUID();
        final var request = AtualizarUsuarioRequest.builder()
                .nome("Novo Nome")
                .nomeUsuario("novo_username")
                .dataNascimento(LocalDate.of(2000, 1, 1))
                .build();

        final var entity = new UsuarioEntity();

        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(jpaRepository.save(entity)).thenReturn(entity);

        repository.atualizarUsuarioParcialmente(id, request);

        final var inOrder = inOrder(jpaRepository, usuarioMapper);

        inOrder.verify(jpaRepository).findById(id);
        inOrder.verify(usuarioMapper).updateEntity(request, entity);
        inOrder.verify(jpaRepository).save(entity);
    }

    @Test
    @DisplayName("Deve achar usuário pelo id")
    void deveAcharUsuarioPeloId() {
        final UUID id = UUID.randomUUID();

        final UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .id(id)
                .nomeUsuario("joao")
                .build();

        when(jpaRepository.findById(id)).thenReturn(Optional.of(usuarioEntity));

        final Optional<UsuarioEntity> resultado = repository.acharPeloId(id);

        assertTrue(resultado.isPresent());

        assertEquals(id, resultado.get().getId());
        assertEquals("joao", resultado.get().getNomeUsuario());

        verify(jpaRepository).findById(id);
    }

    @Test
    @DisplayName("Deve retornar optional vazio ao buscar usuário inexistente pelo id")
    void deveRetornarOptionalVazioAoBuscarUsuarioInexistentePeloId() {
        final UUID id = UUID.randomUUID();

        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        final Optional<UsuarioEntity> resultado = repository.acharPeloId(id);

        assertTrue(resultado.isEmpty());

        verify(jpaRepository).findById(id);
    }

    @Test
    @DisplayName("Deve atualizar foto de perfil do usuário")
    void deveAtualizarFotoPerfilUsuario() {
        final UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .nomeUsuario("maria")
                .caminhoDaImagemDePerfil(null)
                .build();

        final String novoCaminho = "usuarios/maria/perfil/foto.png";

        repository.atualizarFotoPerfilUsuario(usuarioEntity, novoCaminho);

        assertEquals(novoCaminho, usuarioEntity.getCaminhoDaImagemDePerfil());

        verify(jpaRepository).save(usuarioEntity);
    }

    @Test
    @DisplayName("Deve sobrescrever foto de perfil existente")
    void deveSobrescreverFotoPerfilExistente() {
        final UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .nomeUsuario("ana")
                .caminhoDaImagemDePerfil("usuarios/ana/perfil/antiga.png")
                .build();

        final String novoCaminho = "usuarios/ana/perfil/nova.png";

        repository.atualizarFotoPerfilUsuario(usuarioEntity, novoCaminho);

        assertEquals(novoCaminho, usuarioEntity.getCaminhoDaImagemDePerfil());

        verify(jpaRepository).save(usuarioEntity);
    }

    @Test
    @DisplayName("Deve atualizar foto de perfil para null")
    void deveAtualizarFotoPerfilParaNull() {
        final UsuarioEntity usuarioEntity = UsuarioEntity.builder()
                .nomeUsuario("carlos")
                .caminhoDaImagemDePerfil("usuarios/carlos/perfil/foto.png")
                .build();

        repository.atualizarFotoPerfilUsuario(usuarioEntity, null);

        assertNull(usuarioEntity.getCaminhoDaImagemDePerfil());

        verify(jpaRepository).save(usuarioEntity);
    }
}