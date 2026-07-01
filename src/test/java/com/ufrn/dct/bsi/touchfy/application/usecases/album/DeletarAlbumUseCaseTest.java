package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeletarAlbumUseCaseTest {

    @Test
    void deveDeletarAlbumQuandoUsuarioForArtistaDono() {
        final AlbumRepository repository = mock(AlbumRepository.class);
        final DeletarAlbumUseCase useCase = new DeletarAlbumUseCase(repository);
        final UUID albumId = UUID.randomUUID();
        final UUID artistaId = UUID.randomUUID();
        final var album = new Album(albumId, "Nome", null, null, null,
                artistaId, List.of());

        when(repository.acharPeloId(albumId)).thenReturn(Optional.of(album));

        useCase.execute(albumId, artistaId);

        verify(repository).deletar(albumId);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForDono() {
        final AlbumRepository repository = mock(AlbumRepository.class);
        final DeletarAlbumUseCase useCase = new DeletarAlbumUseCase(repository);
        final UUID albumId = UUID.randomUUID();
        final var album = new Album(albumId, "Nome", null, null, null,
                UUID.randomUUID(), List.of());

        when(repository.acharPeloId(albumId)).thenReturn(Optional.of(album));

        assertThrows(RuntimeException.class,
                () -> useCase.execute(albumId, UUID.randomUUID()));
    }
}
