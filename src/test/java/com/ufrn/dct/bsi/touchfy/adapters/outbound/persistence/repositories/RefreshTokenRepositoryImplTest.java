package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.RefreshTokenEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.RefreshTokenJpaRepository;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RefreshTokenRepositoryImplTest {

  private RefreshTokenJpaRepository jpaRepository;
  private UsuarioMapper usuarioMapper;

  private RefreshTokenRepositoryImpl repository;

  private final long refreshExpiration = 1000 * 60 * 60 * 24;

  @BeforeEach
  void setUp() {
    jpaRepository = mock(RefreshTokenJpaRepository.class);
    usuarioMapper = mock(UsuarioMapper.class);

    repository = new RefreshTokenRepositoryImpl(jpaRepository, usuarioMapper, refreshExpiration);
  }

  @Test
  void deveAcharTokenPeloValor() {
    final String token = "refresh.token";

    final RefreshTokenEntity entity =
        RefreshTokenEntity.builder().token(token).revogado(false).expiracao(Instant.now()).build();

    when(jpaRepository.findByToken(token)).thenReturn(Optional.of(entity));

    final Optional<RefreshTokenEntity> resultado = repository.acharPeloToken(token);

    assertTrue(resultado.isPresent());
    assertEquals(token, resultado.get().getToken());

    verify(jpaRepository).findByToken(token);
  }

  @Test
  void deveSalvarRefreshToken() {
    final Usuario usuario = criarUsuario();
    final UsuarioEntity usuarioEntity = criarUsuarioEntity();

    final String token = "refresh.token";

    when(usuarioMapper.toEntity(usuario)).thenReturn(usuarioEntity);

    repository.salvar(usuario, token);

    verify(usuarioMapper).toEntity(usuario);

    verify(jpaRepository)
        .save(
            argThat(
                entity ->
                    entity.getToken().equals(token)
                        && entity.getUsuario().equals(usuarioEntity)
                        && !entity.getRevogado()
                        && entity.getExpiracao().isAfter(Instant.now())));
  }

  @Test
  void deveRevogarToken() {
    final String token = "refresh.token";

    final RefreshTokenEntity entity =
        RefreshTokenEntity.builder().token(token).revogado(false).expiracao(Instant.now()).build();

    when(jpaRepository.findByToken(token)).thenReturn(Optional.of(entity));

    repository.revogar(token);

    assertTrue(entity.getRevogado());

    verify(jpaRepository).findByToken(token);
    verify(jpaRepository).save(entity);
  }

  @Test
  void deveLancarExcecaoAoRevogarTokenInexistente() {
    final String token = "token.inexistente";

    when(jpaRepository.findByToken(token)).thenReturn(Optional.empty());

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> repository.revogar(token));

    assertEquals("Token não encontrado", exception.getMessage());

    verify(jpaRepository).findByToken(token);
    verify(jpaRepository, never()).save(any());
  }

  private Usuario criarUsuario() {
    return new Usuario(
        UUID.randomUUID(),
        "Usuário",
        "usuario_teste",
        "senha",
        new Email("teste@email.com"),
        null,
        true,
        LocalDate.now(),
        null,
        Set.of());
  }

  private UsuarioEntity criarUsuarioEntity() {
    return UsuarioEntity.builder()
        .id(UUID.randomUUID())
        .nome("Usuário")
        .nomeUsuario("usuario_teste")
        .senha("senha")
        .email(new Email("teste@email.com"))
        .build();
  }
}
