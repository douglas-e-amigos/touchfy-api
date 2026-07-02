package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.application.dtos.album.AtualizarAlbumRequest;
import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import com.ufrn.dct.bsi.touchfy.domain.album.models.TipoAlbum;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AtualizarAlbumUseCaseTest {

  @Test
  void deveAtualizarAlbumQuandoUsuarioForArtistaDono() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final AtualizarAlbumUseCase useCase = new AtualizarAlbumUseCase(repository);
    final UUID albumId = UUID.randomUUID();
    final UUID artistaId = UUID.randomUUID();
    final var album = new Album(albumId, "Nome", null, null, null, artistaId, null, List.of());
    final var request =
        new AtualizarAlbumRequest("Novo Nome", "Nova Desc", null, null, TipoAlbum.ALBUM);

    when(repository.acharPeloId(albumId)).thenReturn(Optional.of(album));

    useCase.execute(albumId, request, artistaId);

    verify(repository).atualizar(albumId, request);
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoForArtistaDono() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final AtualizarAlbumUseCase useCase = new AtualizarAlbumUseCase(repository);
    final UUID albumId = UUID.randomUUID();
    final var album =
        new Album(albumId, "Nome", null, null, null, UUID.randomUUID(), null, List.of());
    final var request = new AtualizarAlbumRequest("Nome", null, null, null, TipoAlbum.ALBUM);

    when(repository.acharPeloId(albumId)).thenReturn(Optional.of(album));

    assertThrows(
        RuntimeException.class, () -> useCase.execute(albumId, request, UUID.randomUUID()));
  }
}
