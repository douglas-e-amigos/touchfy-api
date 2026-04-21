package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.CriarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;

import lombok.AllArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CriarUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public void execute(final CriarUsuarioRequest request) {
        validarSenha(request.senha(), request.senhaNovamente());
        final var usuario = usuarioMapper.toDomain(request);
        usuario.setSenha(gerarHash(usuario.getSenha()));
        usuarioRepository.salvar(usuario);
    }

    private String gerarHash(final String senha) {
        return passwordEncoder.encode(senha);
    }

    private void validarSenha(final String senha, final String senhaNovamente) {
        if (senha == null || senha.isEmpty() || senhaNovamente == null || senhaNovamente.isEmpty()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }
        if (!senha.equals(senhaNovamente)) {
            throw new IllegalArgumentException("As senhas devem ser iguais.");
        }
    }
}
