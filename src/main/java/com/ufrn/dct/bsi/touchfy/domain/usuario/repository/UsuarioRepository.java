package com.ufrn.dct.bsi.touchfy.domain.usuario.repository;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;

import java.util.Optional;

public interface UsuarioRepository {
    UsuarioEntity salvar(Usuario usuario);

    Optional<UsuarioEntity> acharPeloNomeDeUsuario(final String nomeUsuario);
}
