package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumMusicaEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumSalvoEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.MusicaEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.AlbumMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.AlbumJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.AlbumMusicaJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.AlbumSalvoJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.GeneroMusicalJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.MusicaJpaRepository;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AtualizarAlbumRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.CriarAlbumRequest;
import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import com.ufrn.dct.bsi.touchfy.domain.album.models.TipoAlbum;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RequisicaoInvalidaException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.AuditorAware;

class AlbumRepositoryImplTest {

  private AuditorAware<UUID> auditorAware;
  private AlbumJpaRepository jpaRepository;
  private AlbumMusicaJpaRepository albumMusicaJpaRepository;
  private AlbumSalvoJpaRepository albumSalvoJpaRepository;
  private MusicaJpaRepository musicaJpaRepository;
  private GeneroMusicalJpaRepository generoMusicalJpaRepository;
  private AlbumMapper mapper;
  private AlbumRepositoryImpl repository;

  @BeforeEach
  void setUp() {
    auditorAware = Optional::empty;
    jpaRepository = mock(AlbumJpaRepository.class);
    albumMusicaJpaRepository = mock(AlbumMusicaJpaRepository.class);
    albumSalvoJpaRepository = mock(AlbumSalvoJpaRepository.class);
    musicaJpaRepository = mock(MusicaJpaRepository.class);
    generoMusicalJpaRepository = mock(GeneroMusicalJpaRepository.class);
    mapper = mock(AlbumMapper.class);
    repository =
        new AlbumRepositoryImpl(
            auditorAware,
            jpaRepository,
            albumMusicaJpaRepository,
            albumSalvoJpaRepository,
            musicaJpaRepository,
            generoMusicalJpaRepository,
            mapper);
  }

  @Test
  void deveCriarAlbum() {
    final var request =
        new CriarAlbumRequest("Album", "Desc", LocalDate.now().plusDays(1), null, TipoAlbum.ALBUM);
    final UUID artistaId = UUID.randomUUID();

    repository.criar(request, artistaId);

    verify(jpaRepository).save(any());
  }

  @Test
  void deveLancarExcecaoQuandoDataDeLancamentoForPassado() {
    final var request =
        new CriarAlbumRequest("Album", "Desc", LocalDate.now().minusDays(1), null, TipoAlbum.ALBUM);

    assertThrows(
        RequisicaoInvalidaException.class, () -> repository.criar(request, UUID.randomUUID()));
    verifyNoInteractions(jpaRepository);
  }

  @Test
  void deveLancarExcecaoQuandoGeneroMusicalNaoExistir() {
    final UUID generoId = UUID.randomUUID();
    final var request = new CriarAlbumRequest("Album", "Desc", null, generoId, TipoAlbum.ALBUM);
    when(generoMusicalJpaRepository.findById(generoId)).thenReturn(Optional.empty());

    assertThrows(
        RecursoNaoEncontradoException.class, () -> repository.criar(request, UUID.randomUUID()));
  }

  @Test
  void deveAtualizarAlbum() {
    final UUID id = UUID.randomUUID();
    final var entity = AlbumEntity.builder().id(id).build();
    final var request =
        new AtualizarAlbumRequest(
            "Novo Nome", "Nova Desc", LocalDate.now().plusDays(1), null, TipoAlbum.ALBUM);
    when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

    repository.atualizar(id, request);

    assertEquals("Novo Nome", entity.getNome());
    verify(jpaRepository).save(entity);
  }

  @Test
  void deveLancarExcecaoAoAtualizarQuandoDataDeLancamentoForPassado() {
    final UUID id = UUID.randomUUID();
    final var entity = AlbumEntity.builder().id(id).build();
    final var request =
        new AtualizarAlbumRequest(
            "Nome", "Desc", LocalDate.now().minusDays(1), null, TipoAlbum.ALBUM);
    when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

    assertThrows(RequisicaoInvalidaException.class, () -> repository.atualizar(id, request));
  }

  @Test
  void deveDeletarAlbumComSoftDelete() {
    final UUID auditorId = UUID.randomUUID();
    auditorAware = () -> Optional.of(auditorId);
    setUpComAuditor();
    final UUID id = UUID.randomUUID();
    final var entity = AlbumEntity.builder().id(id).build();
    when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

    repository.deletar(id);

    assertFalse(entity.getAtivo());
    assertNotNull(entity.getDeletadoEm());
    assertEquals(auditorId, entity.getDeletadoPor());
    verify(jpaRepository).save(entity);
  }

  @Test
  void deveAcharAlbumPeloId() {
    final UUID id = UUID.randomUUID();
    final var entity = AlbumEntity.builder().id(id).build();
    final var album = mock(Album.class);
    when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
    when(mapper.toDomain(entity)).thenReturn(album);

    final var resultado = repository.acharPeloId(id);

    assertTrue(resultado.isPresent());
    assertEquals(album, resultado.get());
  }

  @Test
  void deveRetornarVazioQuandoAlbumNaoExistir() {
    when(jpaRepository.findById(any())).thenReturn(Optional.empty());

    assertTrue(repository.acharPeloId(UUID.randomUUID()).isEmpty());
  }

  @Test
  void deveListarAlbuns() {
    final var entity = AlbumEntity.builder().id(UUID.randomUUID()).build();
    when(jpaRepository.findAll()).thenReturn(List.of(entity));
    when(mapper.toDomain(entity)).thenReturn(mock(Album.class));

    final var resultado = repository.listar();

    assertEquals(1, resultado.size());
  }

  @Test
  void deveAdicionarMusicaNoAlbum() {
    final UUID albumId = UUID.randomUUID();
    final UUID musicaId = UUID.randomUUID();
    final var albumEntity = AlbumEntity.builder().id(albumId).build();
    final var musicaEntity = MusicaEntity.builder().id(musicaId).build();
    when(jpaRepository.findById(albumId)).thenReturn(Optional.of(albumEntity));
    when(musicaJpaRepository.findById(musicaId)).thenReturn(Optional.of(musicaEntity));

    repository.adicionarMusica(albumId, musicaId, 1);

    verify(jpaRepository).save(albumEntity);
    assertEquals(1, albumEntity.getMusicas().size());
  }

  @Test
  void deveLancarExcecaoAoAdicionarMusicaInexistente() {
    when(jpaRepository.findById(any())).thenReturn(Optional.of(AlbumEntity.builder().build()));
    when(musicaJpaRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(
        RecursoNaoEncontradoException.class,
        () -> repository.adicionarMusica(UUID.randomUUID(), UUID.randomUUID(), 0));
  }

  @Test
  void deveVerificarExistenciaDeMusicaNoAlbum() {
    final UUID albumId = UUID.randomUUID();
    final UUID musicaId = UUID.randomUUID();
    when(albumMusicaJpaRepository.findByAlbumIdAndMusicaId(albumId, musicaId))
        .thenReturn(Optional.of(AlbumMusicaEntity.builder().build()));

    assertTrue(repository.existeMusicaNoAlbum(albumId, musicaId));
  }

  @Test
  void deveSalvarAlbum() {
    final UUID albumId = UUID.randomUUID();
    final UUID usuarioId = UUID.randomUUID();
    final var entity = AlbumEntity.builder().id(albumId).build();
    when(jpaRepository.findById(albumId)).thenReturn(Optional.of(entity));

    repository.salvar(albumId, usuarioId);

    verify(albumSalvoJpaRepository).save(any(AlbumSalvoEntity.class));
  }

  @Test
  void deveRemoverAlbumSalvoComSoftDelete() {
    final UUID auditorId = UUID.randomUUID();
    auditorAware = () -> Optional.of(auditorId);
    setUpComAuditor();
    final UUID albumId = UUID.randomUUID();
    final UUID usuarioId = UUID.randomUUID();
    final var salvoEntity = AlbumSalvoEntity.builder().build();
    when(albumSalvoJpaRepository.findByAlbumIdAndUsuarioId(albumId, usuarioId))
        .thenReturn(Optional.of(salvoEntity));

    repository.removerSalvo(albumId, usuarioId);

    assertFalse(salvoEntity.getAtivo());
    assertNotNull(salvoEntity.getDeletadoEm());
    assertEquals(auditorId, salvoEntity.getDeletadoPor());
    verify(albumSalvoJpaRepository).save(salvoEntity);
  }

  @Test
  void deveVerificarExistenciaDeAlbumSalvo() {
    when(albumSalvoJpaRepository.existsByAlbumIdAndUsuarioId(any(), any())).thenReturn(true);

    assertTrue(repository.existeAlbumSalvo(UUID.randomUUID(), UUID.randomUUID()));
  }

  private void setUpComAuditor() {
    repository =
        new AlbumRepositoryImpl(
            auditorAware,
            jpaRepository,
            albumMusicaJpaRepository,
            albumSalvoJpaRepository,
            musicaJpaRepository,
            generoMusicalJpaRepository,
            mapper);
  }
}
