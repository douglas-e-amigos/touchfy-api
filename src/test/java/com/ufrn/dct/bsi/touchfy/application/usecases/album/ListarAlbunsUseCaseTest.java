package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.AlbumMapper;
import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ListarAlbunsUseCaseTest {

  @Test
  void deveListarTodosOsAlbuns() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final AlbumMapper mapper = mock(AlbumMapper.class);
    final ListarAlbunsUseCase useCase = new ListarAlbunsUseCase(repository, mapper);
    final var album =
        new Album(UUID.randomUUID(), "Album", null, null, null, UUID.randomUUID(), null, List.of());

    when(repository.listar()).thenReturn(List.of(album));

    final var resultado = useCase.execute();

    assertEquals(1, resultado.size());
  }
}
