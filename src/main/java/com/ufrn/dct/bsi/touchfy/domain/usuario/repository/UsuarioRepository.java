package com.ufrn.dct.bsi.touchfy.domain.usuario.repository;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;

public interface UsuarioRepository {
    UsuarioEntity salvar(Usuario usuario);
}
