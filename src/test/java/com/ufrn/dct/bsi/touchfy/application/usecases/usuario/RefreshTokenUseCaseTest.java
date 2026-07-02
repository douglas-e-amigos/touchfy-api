package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.RefreshTokenEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.TokenResponse;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.RefreshTokenRepository;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import com.ufrn.dct.bsi.touchfy.infrastructure.security.TokenService;
import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RefreshTokenUseCaseTest {

  private RefreshTokenRepository refreshTokenRepository;
  private UsuarioRepository usuarioRepository;
  private UsuarioMapper usuarioMapper;
  private TokenService tokenService;

  private RefreshTokenUseCase useCase;

  @BeforeEach
  void setUp() {
    refreshTokenRepository = mock(RefreshTokenRepository.class);
    usuarioRepository = mock(UsuarioRepository.class);
    usuarioMapper = mock(UsuarioMapper.class);
    tokenService = mock(TokenService.class);

    useCase =
        new RefreshTokenUseCase(
            refreshTokenRepository, usuarioRepository, usuarioMapper, tokenService);
  }

  @Test
  void deveGerarNovoAccessToken() {
    final String refreshToken = "refresh.token";
    final String username = "usuario";
    final String novoAccessToken = "novo.access.token";

    final RefreshTokenEntity refreshEntity =
        criarRefreshTokenEntity(false, Instant.now().plusSeconds(3600));

    final UsuarioEntity usuarioEntity = criarUsuarioEntity();
    final Usuario usuarioDomain = criarUsuarioDomain();

    when(refreshTokenRepository.acharPeloToken(refreshToken))
        .thenReturn(Optional.of(refreshEntity));

    when(tokenService.isRefreshTokenValid(refreshToken)).thenReturn(true);

    when(tokenService.getRefreshSubject(refreshToken)).thenReturn(username);

    when(usuarioRepository.acharPeloNomeDeUsuario(username)).thenReturn(Optional.of(usuarioEntity));

    when(usuarioMapper.toDomain(usuarioEntity)).thenReturn(usuarioDomain);

    when(tokenService.generateAccessToken(usuarioDomain)).thenReturn(novoAccessToken);

    final TokenResponse response = useCase.execute(refreshToken);

    assertNotNull(response);

    assertEquals(novoAccessToken, response.accessToken());

    verify(refreshTokenRepository).acharPeloToken(refreshToken);

    verify(tokenService).isRefreshTokenValid(refreshToken);

    verify(tokenService).getRefreshSubject(refreshToken);

    verify(usuarioRepository).acharPeloNomeDeUsuario(username);

    verify(usuarioMapper).toDomain(usuarioEntity);

    verify(tokenService).generateAccessToken(usuarioDomain);
  }

  @Test
  void deveLancarExcecaoQuandoRefreshTokenNaoExistir() {
    final String refreshToken = "refresh.token";

    when(refreshTokenRepository.acharPeloToken(refreshToken)).thenReturn(Optional.empty());

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(refreshToken));

    assertEquals("Refresh token invalido", exception.getMessage());

    verify(refreshTokenRepository).acharPeloToken(refreshToken);

    verifyNoInteractions(tokenService);
  }

  @Test
  void deveLancarExcecaoQuandoRefreshTokenEstiverRevogado() {
    final String refreshToken = "refresh.token";

    final RefreshTokenEntity entity =
        criarRefreshTokenEntity(true, Instant.now().plusSeconds(3600));

    when(refreshTokenRepository.acharPeloToken(refreshToken)).thenReturn(Optional.of(entity));

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(refreshToken));

    assertEquals("Refresh token revogado", exception.getMessage());

    verify(refreshTokenRepository).acharPeloToken(refreshToken);

    verifyNoInteractions(tokenService);
  }

  @Test
  void deveLancarExcecaoQuandoRefreshTokenEstiverExpirado() {
    final String refreshToken = "refresh.token";

    final RefreshTokenEntity entity =
        criarRefreshTokenEntity(false, Instant.now().minusSeconds(3600));

    when(refreshTokenRepository.acharPeloToken(refreshToken)).thenReturn(Optional.of(entity));

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(refreshToken));

    assertEquals("Refresh token expirado", exception.getMessage());

    verify(refreshTokenRepository).acharPeloToken(refreshToken);

    verifyNoInteractions(tokenService);
  }

  @Test
  void deveLancarExcecaoQuandoRefreshTokenForInvalido() {
    final String refreshToken = "refresh.token";

    final RefreshTokenEntity entity =
        criarRefreshTokenEntity(false, Instant.now().plusSeconds(3600));

    when(refreshTokenRepository.acharPeloToken(refreshToken)).thenReturn(Optional.of(entity));

    when(tokenService.isRefreshTokenValid(refreshToken)).thenReturn(false);

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(refreshToken));

    assertEquals("Refresh token invalido", exception.getMessage());

    verify(tokenService).isRefreshTokenValid(refreshToken);
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {
    final String refreshToken = "refresh.token";
    final String username = "usuario";

    final RefreshTokenEntity entity =
        criarRefreshTokenEntity(false, Instant.now().plusSeconds(3600));

    when(refreshTokenRepository.acharPeloToken(refreshToken)).thenReturn(Optional.of(entity));

    when(tokenService.isRefreshTokenValid(refreshToken)).thenReturn(true);

    when(tokenService.getRefreshSubject(refreshToken)).thenReturn(username);

    when(usuarioRepository.acharPeloNomeDeUsuario(username)).thenReturn(Optional.empty());

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(refreshToken));

    assertEquals("Usuário não encontrado", exception.getMessage());

    verify(usuarioRepository).acharPeloNomeDeUsuario(username);
  }

  private RefreshTokenEntity criarRefreshTokenEntity(
      final boolean revogado, final Instant expiracao) {
    return RefreshTokenEntity.builder()
        .token("refresh.token")
        .revogado(revogado)
        .expiracao(expiracao)
        .build();
  }

  private UsuarioEntity criarUsuarioEntity() {
    return UsuarioEntity.builder()
        .id(UUID.randomUUID())
        .nome("Usuário")
        .nomeUsuario("usuario")
        .senha("senha")
        .email(new Email("teste@email.com"))
        .build();
  }

  private Usuario criarUsuarioDomain() {
    return new Usuario(
        UUID.randomUUID(),
        "Usuário",
        "usuario",
        "senha",
        new Email("teste@email.com"),
        null,
        true,
        LocalDate.now(),
        null,
        Set.of());
  }
}
