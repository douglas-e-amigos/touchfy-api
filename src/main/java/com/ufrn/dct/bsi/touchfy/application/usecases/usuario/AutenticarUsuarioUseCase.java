package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.TokenResponse;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.RefreshTokenRepository;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import com.ufrn.dct.bsi.touchfy.infrastructure.security.PasswordEncoder;
import com.ufrn.dct.bsi.touchfy.infrastructure.security.TokenService;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.NaoAutenticadoException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AutenticarUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UsuarioMapper usuarioMapper;


    public TokenResponse execute(final String nomeUsuario, final String senhaUsuario) {
        final var usuarioEntity = usuarioRepository.acharPeloNomeDeUsuario(nomeUsuario)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senhaUsuario, usuarioEntity.getSenha())) {
            throw new NaoAutenticadoException("Credenciais inválidas");
        }

        final Usuario usuario = usuarioMapper.toDomain(usuarioEntity);
        final String accessToken = tokenService.generateAccessToken(usuario);
        final String refreshToken = tokenService.generateRefreshToken(usuario);

        refreshTokenRepository.salvar(usuario, refreshToken);

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
