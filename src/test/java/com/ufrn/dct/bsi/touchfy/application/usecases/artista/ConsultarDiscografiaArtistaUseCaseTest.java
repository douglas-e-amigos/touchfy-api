package com.ufrn.dct.bsi.touchfy.application.usecases.artista;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.AlbumMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumResponse;
import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import com.ufrn.dct.bsi.touchfy.domain.album.models.TipoAlbum;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ConsultarDiscografiaArtistaUseCaseTest {

  @Test
  void deveAgruparDiscografiaPorTipo() {
    final AlbumRepository albumRepository = mock(AlbumRepository.class);
    final MusicaRepository musicaRepository = mock(MusicaRepository.class);
    final AlbumMapper albumMapper = mock(AlbumMapper.class);
    final ConsultarDiscografiaArtistaUseCase useCase =
        new ConsultarDiscografiaArtistaUseCase(albumRepository, musicaRepository, albumMapper);
    final UUID artistaId = UUID.randomUUID();

    final Album album =
        Album.builder()
            .id(UUID.randomUUID())
            .nome("Álbum 1")
            .artistaId(artistaId)
            .tipo(TipoAlbum.ALBUM)
            .musicas(List.of())
            .build();
    final Album ep =
        Album.builder()
            .id(UUID.randomUUID())
            .nome("EP 1")
            .artistaId(artistaId)
            .tipo(TipoAlbum.EP)
            .musicas(List.of())
            .build();
    final Album single =
        Album.builder()
            .id(UUID.randomUUID())
            .nome("Single 1")
            .artistaId(artistaId)
            .tipo(TipoAlbum.SINGLE)
            .musicas(List.of())
            .build();

    when(albumRepository.listarPorArtista(artistaId)).thenReturn(List.of(album, ep, single));
    when(musicaRepository.consultarPorCriadoPor(artistaId)).thenReturn(List.of());

    final var albumResp =
        new AlbumResponse(
            album.getId(),
            album.getNome(),
            null,
            null,
            null,
            artistaId,
            TipoAlbum.ALBUM,
            List.of(),
            null,
            null);
    final var epResp =
        new AlbumResponse(
            ep.getId(),
            ep.getNome(),
            null,
            null,
            null,
            artistaId,
            TipoAlbum.EP,
            List.of(),
            null,
            null);
    final var singleResp =
        new AlbumResponse(
            single.getId(),
            single.getNome(),
            null,
            null,
            null,
            artistaId,
            TipoAlbum.SINGLE,
            List.of(),
            null,
            null);

    when(albumMapper.toResponse(album)).thenReturn(albumResp);
    when(albumMapper.toResponse(ep)).thenReturn(epResp);
    when(albumMapper.toResponse(single)).thenReturn(singleResp);

    final var response = useCase.execute(artistaId);

    assertEquals(1, response.albuns().size());
    assertEquals(1, response.eps().size());
    assertEquals(1, response.singles().size());
    assertTrue(response.compilacoes().isEmpty());
  }

  @Test
  void deveRetornarListasVaziasQuandoNaoHouverDiscografia() {
    final AlbumRepository albumRepository = mock(AlbumRepository.class);
    final MusicaRepository musicaRepository = mock(MusicaRepository.class);
    final AlbumMapper albumMapper = mock(AlbumMapper.class);
    final ConsultarDiscografiaArtistaUseCase useCase =
        new ConsultarDiscografiaArtistaUseCase(albumRepository, musicaRepository, albumMapper);
    final UUID artistaId = UUID.randomUUID();

    when(albumRepository.listarPorArtista(artistaId)).thenReturn(List.of());
    when(musicaRepository.consultarPorCriadoPor(artistaId)).thenReturn(List.of());

    final var response = useCase.execute(artistaId);

    assertTrue(response.albuns().isEmpty());
    assertTrue(response.singles().isEmpty());
    assertTrue(response.eps().isEmpty());
    assertTrue(response.compilacoes().isEmpty());
  }

  @Test
  void deveIncluirMusicasAvulsasComoSingles() {
    final AlbumRepository albumRepository = mock(AlbumRepository.class);
    final MusicaRepository musicaRepository = mock(MusicaRepository.class);
    final AlbumMapper albumMapper = mock(AlbumMapper.class);
    final ConsultarDiscografiaArtistaUseCase useCase =
        new ConsultarDiscografiaArtistaUseCase(albumRepository, musicaRepository, albumMapper);
    final UUID artistaId = UUID.randomUUID();

    final Musica musicaAvulsa =
        Musica.builder()
            .id(UUID.randomUUID())
            .nome("Música Avulsa")
            .criadoPor(artistaId)
            .caminhoDoArquivo("musica.mp3")
            .build();
    final Album singleWrapper =
        Album.builder()
            .id(musicaAvulsa.getId())
            .nome(musicaAvulsa.getNome())
            .artistaId(artistaId)
            .tipo(TipoAlbum.SINGLE)
            .musicas(List.of(musicaAvulsa))
            .build();

    when(albumRepository.listarPorArtista(artistaId)).thenReturn(List.of());
    when(musicaRepository.consultarPorCriadoPor(artistaId)).thenReturn(List.of(musicaAvulsa));
    when(albumMapper.toAlbumDeSingleAvulso(musicaAvulsa)).thenReturn(singleWrapper);

    final var singleResp =
        new AlbumResponse(
            singleWrapper.getId(),
            singleWrapper.getNome(),
            null,
            null,
            null,
            artistaId,
            TipoAlbum.SINGLE,
            List.of(),
            null,
            null);
    when(albumMapper.toResponse(singleWrapper)).thenReturn(singleResp);

    final var response = useCase.execute(artistaId);

    assertEquals(1, response.singles().size());
    assertEquals("Música Avulsa", response.singles().get(0).nome());
  }
}
