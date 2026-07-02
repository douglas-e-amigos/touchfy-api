package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AdicionarMusicaAlbumUseCaseTest {

  @Test
  void deveAdicionarMusicaQuandoArtistaDonoEMusicaNaoEstiverNoAlbum() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final AdicionarMusicaAlbumUseCase useCase = new AdicionarMusicaAlbumUseCase(repository);
    final UUID albumId = UUID.randomUUID();
    final UUID musicaId = UUID.randomUUID();
    final UUID artistaId = UUID.randomUUID();
    final var album = new Album(albumId, "Album", null, null, null, artistaId, null, List.of());

    when(repository.acharPeloId(albumId)).thenReturn(Optional.of(album));
    when(repository.existeMusicaNoAlbum(albumId, musicaId)).thenReturn(false);

    useCase.execute(albumId, musicaId, artistaId);

    verify(repository).adicionarMusica(albumId, musicaId, 0);
  }

  @Test
  void deveLancarExcecaoQuandoMusicaJaEstiverNoAlbum() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final AdicionarMusicaAlbumUseCase useCase = new AdicionarMusicaAlbumUseCase(repository);
    final UUID albumId = UUID.randomUUID();
    final UUID musicaId = UUID.randomUUID();
    final UUID artistaId = UUID.randomUUID();
    final var album = new Album(albumId, "Album", null, null, null, artistaId, null, List.of());

    when(repository.acharPeloId(albumId)).thenReturn(Optional.of(album));
    when(repository.existeMusicaNoAlbum(albumId, musicaId)).thenReturn(true);

    assertThrows(RuntimeException.class, () -> useCase.execute(albumId, musicaId, artistaId));
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoForArtistaDono() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final AdicionarMusicaAlbumUseCase useCase = new AdicionarMusicaAlbumUseCase(repository);
    final UUID albumId = UUID.randomUUID();
    final var album =
        new Album(albumId, "Album", null, null, null, UUID.randomUUID(), null, List.of());

    when(repository.acharPeloId(albumId)).thenReturn(Optional.of(album));

    assertThrows(
        RuntimeException.class,
        () -> useCase.execute(albumId, UUID.randomUUID(), UUID.randomUUID()));
  }
}
