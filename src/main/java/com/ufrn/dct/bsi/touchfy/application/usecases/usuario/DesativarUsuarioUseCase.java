package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.security.UsuarioDetalhesImpl;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DesativarUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;

    public void execute(final UUID idUsuario) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new RuntimeException("Usuário não autenticado");
        }

        final Object principal = authentication.getPrincipal();

        if (!(principal instanceof UsuarioDetalhesImpl usuarioDetalhes)) {
            throw new RuntimeException("Usuário não autenticado");
        }

        if (!usuarioDetalhes.getId().equals(idUsuario)) {
            throw new RuntimeException("Usuário não autorizado a desativar esta conta");
        }

        usuarioRepository.deletar(idUsuario);
    }
}