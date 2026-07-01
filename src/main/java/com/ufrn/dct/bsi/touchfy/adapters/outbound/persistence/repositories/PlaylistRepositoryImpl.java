package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.PlaylistEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.PlaylistMusicaEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.PlaylistMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.MusicaJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.PlaylistJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.PlaylistMusicaJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.UsuarioJpaRepository;
import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.AtualizarPlaylistRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.CriarPlaylistRequest;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Playlist;
import com.ufrn.dct.bsi.touchfy.domain.playlist.repositories.PlaylistRepository;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class PlaylistRepositoryImpl implements PlaylistRepository {
  private final AuditorAware<UUID> auditorAware;
  private final PlaylistJpaRepository jpaRepository;
  private final PlaylistMusicaJpaRepository playlistMusicaJpaRepository;
  private final MusicaJpaRepository musicaJpaRepository;
  private final UsuarioJpaRepository usuarioJpaRepository;
  private final PlaylistMapper mapper;

  @Override
  @Transactional
  public void criar(final UUID id, final CriarPlaylistRequest request, final UUID donoId) {
    final var entity =
        PlaylistEntity.builder()
            .id(id)
            .nome(request.nome())
            .descricao(request.descricao())
            .visibilidade(request.visibilidade())
            .donoId(donoId)
            .build();

    if (request.usuariosConvidadosIds() != null && !request.usuariosConvidadosIds().isEmpty()) {
      final Set<UsuarioEntity> convidados =
          new HashSet<>(usuarioJpaRepository.findAllById(request.usuariosConvidadosIds()));
      entity.setUsuariosConvidados(convidados);
    }

    jpaRepository.save(entity);
  }

  @Override
  @Transactional
  public void adicionarMusica(final UUID playlistId, final UUID musicaId, final Integer ordem) {
    final var playlist = acharEntidadePeloId(playlistId);
    final var musica =
        musicaJpaRepository
            .findById(musicaId)
            .orElseThrow(() -> new RuntimeException("Música não encontrada."));

    final var relacao =
        PlaylistMusicaEntity.builder().playlist(playlist).musica(musica).ordem(ordem).build();

    playlist.getMusicas().add(relacao);
    jpaRepository.save(playlist);
  }

  @Override
  @Transactional
  public void removerMusica(final UUID playlistId, final UUID musicaId) {
    final var relacao =
        playlistMusicaJpaRepository
            .findByPlaylistIdAndMusicaId(playlistId, musicaId)
            .orElseThrow(() -> new RuntimeException("Música não encontrada na playlist."));

    relacao.setAtivo(false);
    relacao.setDeletadoEm(LocalDateTime.now());
    auditorAware.getCurrentAuditor().ifPresent(relacao::setDeletadoPor);
    playlistMusicaJpaRepository.save(relacao);
  }

  @Override
  @Transactional
  public void atualizar(final UUID id, final AtualizarPlaylistRequest request) {
    final var entity = acharEntidadePeloId(id);
    entity.setNome(request.nome());
    entity.setDescricao(request.descricao());
    entity.setVisibilidade(request.visibilidade());

    if (request.usuariosConvidadosIds() != null) {
      final Set<UsuarioEntity> convidados =
          request.usuariosConvidadosIds().isEmpty()
              ? new HashSet<>()
              : new HashSet<>(usuarioJpaRepository.findAllById(request.usuariosConvidadosIds()));
      entity.setUsuariosConvidados(convidados);
    }

    jpaRepository.save(entity);
  }

  @Override
  @Transactional
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

  @Override
  @Transactional(readOnly = true)
  public Optional<Playlist> acharPeloId(final UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Playlist> listarVisiveisParaUsuario(final UUID usuarioId) {
    return jpaRepository.findAll().stream()
        .filter(p -> isVisivelPara(p, usuarioId))
        .map(mapper::toDomain)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Playlist> listarDoUsuario(final UUID usuarioId) {
    return jpaRepository.listarDoUsuario(usuarioId).stream().map(mapper::toDomain).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existeMusicaNaPlaylist(final UUID playlistId, final UUID musicaId) {
    return playlistMusicaJpaRepository
        .findByPlaylistIdAndMusicaId(playlistId, musicaId)
        .isPresent();
  }

  private PlaylistEntity acharEntidadePeloId(final UUID id) {
    return jpaRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Playlist não encontrada."));
  }

  private boolean isVisivelPara(final PlaylistEntity playlist, final UUID usuarioId) {
    return switch (playlist.getVisibilidade()) {
      case PUBLICA -> true;
      case PRIVADA -> playlist.getDonoId().equals(usuarioId);
      case PROTEGIDA ->
          playlist.getDonoId().equals(usuarioId)
              || (playlist.getUsuariosConvidados() != null
                  && playlist.getUsuariosConvidados().stream()
                      .anyMatch(u -> u.getId().equals(usuarioId)));
    };
  }
}
