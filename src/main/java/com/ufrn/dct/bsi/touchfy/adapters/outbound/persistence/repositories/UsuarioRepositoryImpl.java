package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.UsuarioJpaRepository;

import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private final UsuarioJpaRepository jpaRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public UsuarioEntity salvar(final Usuario usuario) {
        return jpaRepository.save(usuarioMapper.toEntity(usuario));
    }
}
