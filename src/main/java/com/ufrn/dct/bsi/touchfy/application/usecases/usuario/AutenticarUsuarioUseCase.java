package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import com.ufrn.dct.bsi.touchfy.infrastructure.security.PasswordEncoder;
import com.ufrn.dct.bsi.touchfy.infrastructure.security.TokenService;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AutenticarUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UsuarioMapper usuarioMapper;

    public String execute(final String nomeUsuario, final String senhaUsuario) {
        final var usuario = usuarioRepository.acharPeloNomeDeUsuario(nomeUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(senhaUsuario, usuario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        return tokenService.generateToken(usuarioMapper.toDomain(usuario));
    }
}
