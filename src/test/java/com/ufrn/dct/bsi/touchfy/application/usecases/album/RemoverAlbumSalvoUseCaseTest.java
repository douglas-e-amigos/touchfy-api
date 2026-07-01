package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RemoverAlbumSalvoUseCaseTest {

  @Test
  void deveRemoverAlbumSalvo() {
    final AlbumRepository repository = mock(AlbumRepository.class);
    final RemoverAlbumSalvoUseCase useCase = new RemoverAlbumSalvoUseCase(repository);
    final UUID albumId = UUID.randomUUID();
    final UUID usuarioId = UUID.randomUUID();

    useCase.execute(albumId, usuarioId);

    verify(repository).removerSalvo(albumId, usuarioId);
  }
}
