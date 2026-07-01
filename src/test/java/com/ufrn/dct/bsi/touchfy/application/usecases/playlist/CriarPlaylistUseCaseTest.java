package com.ufrn.dct.bsi.touchfy.application.usecases.playlist;

import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.CriarPlaylistRequest;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Visibilidade;
import com.ufrn.dct.bsi.touchfy.domain.playlist.repositories.PlaylistRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CriarPlaylistUseCaseTest {

    @Test
    void deveCriarPlaylistQuandoRequestForValido() {
        final PlaylistRepository repository = mock(PlaylistRepository.class);
        final CriarPlaylistUseCase useCase = new CriarPlaylistUseCase(repository);
        final UUID usuarioId = UUID.randomUUID();
        final var request = new CriarPlaylistRequest("Minha Playlist", "Desc",
                Visibilidade.PUBLICA, List.of());

        useCase.execute(request, usuarioId);

        verify(repository).criar(any(UUID.class), eq(request), eq(usuarioId));
    }

    @Test
    void deveLancarExcecaoQuandoRequestForNulo() {
        final PlaylistRepository repository = mock(PlaylistRepository.class);
        final CriarPlaylistUseCase useCase = new CriarPlaylistUseCase(repository);

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(null, UUID.randomUUID()));
    }

    @Test
    void deveLancarExcecaoQuandoNomeForVazio() {
        final PlaylistRepository repository = mock(PlaylistRepository.class);
        final CriarPlaylistUseCase useCase = new CriarPlaylistUseCase(repository);
        final var request = new CriarPlaylistRequest("", null, Visibilidade.PUBLICA, List.of());

        assertThrows(IllegalArgumentException.class,
                () -> useCase.execute(request, UUID.randomUUID()));
    }
}
