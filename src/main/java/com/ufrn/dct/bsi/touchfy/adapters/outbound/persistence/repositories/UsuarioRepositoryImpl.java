package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.UsuarioJpaRepository;

import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.AtualizarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@AllArgsConstructor
public class UsuarioRepositoryImpl implements UsuarioRepository {
    private final UsuarioJpaRepository jpaRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public UsuarioEntity salvar(final Usuario usuario) {
        return jpaRepository.save(usuarioMapper.toEntity(usuario));
    }

    @Override
    public Optional<UsuarioEntity> acharPeloNomeDeUsuario(final String nomeUsuario) {
        return jpaRepository.findByNomeUsuario(nomeUsuario);
    }

    @Override
    public void atualizarUsuarioParcialmente(final UUID id, final AtualizarUsuarioRequest request) {
        final var usuarioEntity = jpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        usuarioMapper.updateEntity(request, usuarioEntity);
        jpaRepository.save(usuarioEntity);
    }
}
