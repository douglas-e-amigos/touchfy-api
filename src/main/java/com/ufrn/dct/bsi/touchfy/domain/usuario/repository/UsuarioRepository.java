package com.ufrn.dct.bsi.touchfy.domain.usuario.repository;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.application.dtos.artista.AtualizarDadosArtistaRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.AtualizarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository {
  UsuarioEntity salvar(Usuario usuario, ERole role);

  Optional<UsuarioEntity> acharPeloNomeDeUsuario(final String nomeUsuario);

  Optional<UsuarioEntity> acharPeloId(final UUID id);

  void atualizarUsuarioParcialmente(final UUID id, final AtualizarUsuarioRequest request);

  void atualizarFotoPerfilUsuario(final UsuarioEntity usuarioEntity, final String pathFotoPerfil);

  Optional<Usuario> buscarPorNomeUsuario(final String nomeUsuario);

  void deletar(UUID id);

  void adicionarRole(UUID usuarioId, ERole role);

  void removerRole(UUID usuarioId, ERole role);

  void atualizarDadosArtista(UUID id, AtualizarDadosArtistaRequest request);
}
