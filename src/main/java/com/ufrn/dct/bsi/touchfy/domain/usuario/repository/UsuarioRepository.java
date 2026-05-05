package com.ufrn.dct.bsi.touchfy.domain.usuario.repository;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.AtualizarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository {
    UsuarioEntity salvar(Usuario usuario);

    Optional<UsuarioEntity> acharPeloNomeDeUsuario(final String nomeUsuario);

    void atualizarUsuarioParcialmente(final UUID id, final AtualizarUsuarioRequest request);
}
