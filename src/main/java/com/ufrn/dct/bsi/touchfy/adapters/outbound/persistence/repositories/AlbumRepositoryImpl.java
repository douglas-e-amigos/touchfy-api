package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumMusicaEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumSalvoEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.GeneroMusicalEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.AlbumMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.AlbumJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.AlbumMusicaJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.AlbumSalvoJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.GeneroMusicalJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.MusicaJpaRepository;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AtualizarAlbumRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.CriarAlbumRequest;
import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import com.ufrn.dct.bsi.touchfy.domain.album.models.AlbumSalvo;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RequisicaoInvalidaException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class AlbumRepositoryImpl implements AlbumRepository {
  private final AuditorAware<UUID> auditorAware;
  private final AlbumJpaRepository jpaRepository;
  private final AlbumMusicaJpaRepository albumMusicaJpaRepository;
  private final AlbumSalvoJpaRepository albumSalvoJpaRepository;
  private final MusicaJpaRepository musicaJpaRepository;
  private final GeneroMusicalJpaRepository generoMusicalJpaRepository;
  private final AlbumMapper mapper;

  @Override
  @Transactional
  public void criar(final CriarAlbumRequest request, final UUID artistaId) {
    final var builder =
        AlbumEntity.builder()
            .nome(request.nome())
            .descricao(request.descricao())
            .artistaId(artistaId);

    if (request.dataLancamento() != null) {
      if (request.dataLancamento().isBefore(LocalDate.now())) {
        throw new RequisicaoInvalidaException("Data de lançamento não pode estar no passado.");
      }
      builder.dataLancamento(request.dataLancamento());
    }

    if (request.generoMusicalId() != null) {
      final GeneroMusicalEntity genero =
          generoMusicalJpaRepository
              .findById(request.generoMusicalId())
              .orElseThrow(
                  () -> new RecursoNaoEncontradoException("Gênero musical não encontrado."));
      builder.generoMusical(genero);
    }

    jpaRepository.save(builder.build());
  }

  @Override
  @Transactional
  public void atualizar(final UUID id, final AtualizarAlbumRequest request) {
    final var entity = acharEntidadePeloId(id);
    entity.setNome(request.nome());
    entity.setDescricao(request.descricao());

    if (request.dataLancamento() != null) {
      if (request.dataLancamento().isBefore(LocalDate.now())) {
        throw new RequisicaoInvalidaException("Data de lançamento não pode estar no passado.");
      }
      entity.setDataLancamento(request.dataLancamento());
    }

    if (request.generoMusicalId() != null) {
      final GeneroMusicalEntity genero =
          generoMusicalJpaRepository
              .findById(request.generoMusicalId())
              .orElseThrow(
                  () -> new RecursoNaoEncontradoException("Gênero musical não encontrado."));
      entity.setGeneroMusical(genero);
    } else {
      entity.setGeneroMusical(null);
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
  public Optional<Album> acharPeloId(final UUID id) {
    return jpaRepository.findById(id).map(mapper::toDomain);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Album> listar() {
    return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
  }

  @Override
  @Transactional
  public void adicionarMusica(final UUID albumId, final UUID musicaId, final Integer ordem) {
    final var album = acharEntidadePeloId(albumId);
    final var musica =
        musicaJpaRepository
            .findById(musicaId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Música não encontrada."));

    final var relacao =
        AlbumMusicaEntity.builder().album(album).musica(musica).ordem(ordem).build();

    album.getMusicas().add(relacao);
    jpaRepository.save(album);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existeMusicaNoAlbum(final UUID albumId, final UUID musicaId) {
    return albumMusicaJpaRepository.findByAlbumIdAndMusicaId(albumId, musicaId).isPresent();
  }

  @Override
  @Transactional
  public void salvar(final UUID albumId, final UUID usuarioId) {
    final var album = acharEntidadePeloId(albumId);
    final var salvo = AlbumSalvoEntity.builder().album(album).usuarioId(usuarioId).build();
    albumSalvoJpaRepository.save(salvo);
  }

  @Override
  @Transactional
  public void removerSalvo(final UUID albumId, final UUID usuarioId) {
    albumSalvoJpaRepository
        .findByAlbumIdAndUsuarioId(albumId, usuarioId)
        .ifPresent(
            entity -> {
              entity.setAtivo(false);
              entity.setDeletadoEm(LocalDateTime.now());
              auditorAware.getCurrentAuditor().ifPresent(entity::setDeletadoPor);
              albumSalvoJpaRepository.save(entity);
            });
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existeAlbumSalvo(final UUID albumId, final UUID usuarioId) {
    return albumSalvoJpaRepository.existsByAlbumIdAndUsuarioId(albumId, usuarioId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<AlbumSalvo> listarSalvos(final UUID usuarioId) {
    return albumSalvoJpaRepository.findByUsuarioId(usuarioId).stream()
        .map(mapper::toSalvoDomain)
        .toList();
  }

  private AlbumEntity acharEntidadePeloId(final UUID id) {
    return jpaRepository
        .findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Álbum não encontrado."));
  }
}
