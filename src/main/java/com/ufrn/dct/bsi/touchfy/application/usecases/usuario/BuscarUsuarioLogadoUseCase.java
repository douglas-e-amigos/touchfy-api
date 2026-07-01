package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.UsuarioResponse;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.NaoAutenticadoException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BuscarUsuarioLogadoUseCase {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioResponse execute() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new NaoAutenticadoException("Usuário não autenticado");
        }

        final Usuario usuario = usuarioRepository.buscarPorNomeUsuario(authentication.getName())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado"));

        return usuarioMapper.toResponse(usuario);
    }
}