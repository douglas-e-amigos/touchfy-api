package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

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
