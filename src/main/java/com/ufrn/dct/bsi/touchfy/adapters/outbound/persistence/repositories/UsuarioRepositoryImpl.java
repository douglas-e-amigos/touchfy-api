package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.RoleEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.RoleJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.UsuarioJpaRepository;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.AtualizarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UsuarioRepositoryImpl implements UsuarioRepository {
  private final UsuarioJpaRepository jpaRepository;
  private final UsuarioMapper usuarioMapper;
  private final AuditorAware<java.util.UUID> auditorAware;
  private final RoleJpaRepository roleJpaRepository;

  @Override
  public UsuarioEntity salvar(final Usuario usuario, final ERole role) {
    final UsuarioEntity entity = usuarioMapper.toEntity(usuario);

    final RoleEntity roleEntity =
        roleJpaRepository
            .findByName(role)
            .orElseThrow(() -> new RuntimeException("Role não encontrada: " + role));

    entity.setRoles(new HashSet<>(Set.of(roleEntity)));

    return jpaRepository.save(entity);
  }

  @Override
  public Optional<UsuarioEntity> acharPeloNomeDeUsuario(final String nomeUsuario) {
    return jpaRepository.findByNomeUsuario(nomeUsuario);
  }

  @Override
  public Optional<UsuarioEntity> acharPeloId(final UUID id) {
    return jpaRepository.findById(id);
  }

  @Override
  public void atualizarUsuarioParcialmente(final UUID id, final AtualizarUsuarioRequest request) {
    final var usuarioEntity =
        this.acharPeloId(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
    usuarioMapper.updateEntity(request, usuarioEntity);
    jpaRepository.save(usuarioEntity);
  }

  @Override
  public void atualizarFotoPerfilUsuario(
      final UsuarioEntity usuarioEntity, final String pathFotoPerfil) {
    usuarioEntity.setCaminhoDaImagemDePerfil(pathFotoPerfil);
    jpaRepository.save(usuarioEntity);
  }

  @Override
  public Optional<Usuario> buscarPorNomeUsuario(final String nomeUsuario) {
    return jpaRepository.findByNomeUsuario(nomeUsuario).map(usuarioMapper::toDomain);
  }

  @Override
  public void deletar(final UUID id) {
    jpaRepository
        .findById(id)
        .ifPresent(
            entity -> {
              entity.setAtivo(false);
              entity.setDeletadoEm(LocalDateTime.now());
              auditorAware.getCurrentAuditor().ifPresent(entity::setDeletadoPor);
              jpaRepository.save(entity);
            });
  }
}
