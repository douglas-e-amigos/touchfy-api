package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.AlbumMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumResponse;
import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BuscarAlbumUseCaseTest {

  @Test
  void deveRetornarAlbumQuandoExistir() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final AlbumMapper mapper = mock(AlbumMapper.class);
    final BuscarAlbumUseCase useCase = new BuscarAlbumUseCase(repository, mapper);
    final UUID albumId = UUID.randomUUID();
    final var album = new Album(albumId, "Album", null, null, null, UUID.randomUUID(), List.of());
    final var response =
        new AlbumResponse(
            albumId, "Album", null, null, null, UUID.randomUUID(), List.of(), null, null);

    when(repository.acharPeloId(albumId)).thenReturn(Optional.of(album));
    when(mapper.toResponse(album)).thenReturn(response);

    final var resultado = useCase.execute(albumId);

    assertEquals("Album", resultado.nome());
  }

  @Test
  void deveLancarExcecaoQuandoAlbumNaoExistir() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final AlbumMapper mapper = mock(AlbumMapper.class);
    final BuscarAlbumUseCase useCase = new BuscarAlbumUseCase(repository, mapper);

    when(repository.acharPeloId(any())).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> useCase.execute(UUID.randomUUID()));
  }
}
