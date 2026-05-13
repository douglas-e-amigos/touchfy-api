package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.UsuarioResponse;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class BuscarUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioResponse execute(final UUID idUsuario) {
        final UsuarioEntity usuarioEntity = usuarioRepository.acharPeloId(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        final Usuario usuario = usuarioMapper.toDomain(usuarioEntity);
        final UsuarioResponse response = usuarioMapper.toResponse(usuario);
        return response;
    }
}
