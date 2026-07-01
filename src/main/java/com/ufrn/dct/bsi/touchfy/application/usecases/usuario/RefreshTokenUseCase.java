package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.TokenResponse;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.RefreshTokenRepository;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import com.ufrn.dct.bsi.touchfy.infrastructure.security.TokenService;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.NaoAutenticadoException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;
import java.time.Instant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RefreshTokenUseCase {
  private final RefreshTokenRepository refreshTokenRepository;
  private final UsuarioRepository usuarioRepository;
  private final UsuarioMapper usuarioMapper;
  private final TokenService tokenService;

  public TokenResponse execute(final String refreshToken) {
    final var entity =
        refreshTokenRepository
            .acharPeloToken(refreshToken)
            .orElseThrow(() -> new NaoAutenticadoException("Refresh token invalido"));

    if (entity.getRevogado()) {
      throw new NaoAutenticadoException("Refresh token revogado");
    }

    if (entity.getExpiracao().isBefore(Instant.now())) {
      throw new NaoAutenticadoException("Refresh token expirado");
    }

    if (!tokenService.isRefreshTokenValid(refreshToken)) {
      throw new NaoAutenticadoException("Refresh token invalido");
    }

    final String nomeDeUsuario = tokenService.getRefreshSubject(refreshToken);
    final UsuarioEntity usuario =
        usuarioRepository
            .acharPeloNomeDeUsuario(nomeDeUsuario)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));
    final String novoAccessToken =
        tokenService.generateAccessToken(usuarioMapper.toDomain(usuario));

    return TokenResponse.builder().accessToken(novoAccessToken).refreshToken(refreshToken).build();
  }
}
