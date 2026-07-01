package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class SalvarAlbumUseCaseTest {

  @Test
  void deveSalvarAlbumQuandoNaoEstiverSalvo() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final SalvarAlbumUseCase useCase = new SalvarAlbumUseCase(repository);
    final UUID albumId = UUID.randomUUID();
    final UUID usuarioId = UUID.randomUUID();
    final var album = new Album(albumId, "Album", null, null, null, UUID.randomUUID(), List.of());

    when(repository.acharPeloId(albumId)).thenReturn(Optional.of(album));
    when(repository.existeAlbumSalvo(albumId, usuarioId)).thenReturn(false);

    useCase.execute(albumId, usuarioId);

    verify(repository).salvar(albumId, usuarioId);
  }

  @Test
  void deveLancarExcecaoQuandoAlbumJaEstiverSalvo() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final SalvarAlbumUseCase useCase = new SalvarAlbumUseCase(repository);
    final UUID albumId = UUID.randomUUID();
    final UUID usuarioId = UUID.randomUUID();

    when(repository.acharPeloId(albumId)).thenReturn(Optional.of(mock(Album.class)));
    when(repository.existeAlbumSalvo(albumId, usuarioId)).thenReturn(true);

    assertThrows(RuntimeException.class, () -> useCase.execute(albumId, usuarioId));
  }

  @Test
  void deveLancarExcecaoQuandoAlbumNaoExistir() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final SalvarAlbumUseCase useCase = new SalvarAlbumUseCase(repository);

    when(repository.acharPeloId(any())).thenReturn(Optional.empty());

    assertThrows(
        RuntimeException.class, () -> useCase.execute(UUID.randomUUID(), UUID.randomUUID()));
  }
}
