package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.ufrn.dct.bsi.touchfy.application.dtos.album.CriarAlbumRequest;
import com.ufrn.dct.bsi.touchfy.domain.album.models.TipoAlbum;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class CriarAlbumUseCaseTest {

  @Test
  void deveCriarAlbumQuandoRequestForValido() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final CriarAlbumUseCase useCase = new CriarAlbumUseCase(repository);
    final UUID usuarioId = UUID.randomUUID();
    final var request = new CriarAlbumRequest("Meu Álbum", "Desc", null, null, TipoAlbum.ALBUM);

    useCase.execute(request, usuarioId);

    verify(repository).criar(eq(request), eq(usuarioId));
  }

  @Test
  void deveLancarExcecaoQuandoNomeForVazio() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final CriarAlbumUseCase useCase = new CriarAlbumUseCase(repository);

    assertThrows(
        IllegalArgumentException.class,
        () ->
            useCase.execute(
                new CriarAlbumRequest("", null, null, null, TipoAlbum.ALBUM), UUID.randomUUID()));
  }

  @Test
  void deveLancarExcecaoQuandoRequestForNulo() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final CriarAlbumUseCase useCase = new CriarAlbumUseCase(repository);

    assertThrows(IllegalArgumentException.class, () -> useCase.execute(null, UUID.randomUUID()));
  }
}
