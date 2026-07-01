package com.ufrn.dct.bsi.touchfy.application.usecases.playlist;

import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Playlist;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Visibilidade;
import com.ufrn.dct.bsi.touchfy.domain.playlist.repositories.PlaylistRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AdicionarMusicaPlaylistUseCaseTest {

    @Test
    void deveAdicionarMusicaQuandoUsuarioForDonoEMusicaNaoEstiverNaPlaylist() {
        final PlaylistRepository repository = mock(PlaylistRepository.class);
        final AdicionarMusicaPlaylistUseCase useCase = new AdicionarMusicaPlaylistUseCase(repository);
        final UUID playlistId = UUID.randomUUID();
        final UUID musicaId = UUID.randomUUID();
        final UUID donoId = UUID.randomUUID();
        final var playlist = new Playlist(playlistId, "Nome", null, Visibilidade.PUBLICA,
                donoId, List.of(), List.of());

        when(repository.acharPeloId(playlistId)).thenReturn(Optional.of(playlist));
        when(repository.existeMusicaNaPlaylist(playlistId, musicaId)).thenReturn(false);

        useCase.execute(playlistId, musicaId, donoId);

        verify(repository).adicionarMusica(playlistId, musicaId, 0);
    }

    @Test
    void deveLancarExcecaoQuandoMusicaJaEstiverNaPlaylist() {
        final PlaylistRepository repository = mock(PlaylistRepository.class);
        final AdicionarMusicaPlaylistUseCase useCase = new AdicionarMusicaPlaylistUseCase(repository);
        final UUID playlistId = UUID.randomUUID();
        final UUID musicaId = UUID.randomUUID();
        final UUID donoId = UUID.randomUUID();
        final var playlist = new Playlist(playlistId, "Nome", null, Visibilidade.PUBLICA,
                donoId, List.of(), List.of());

        when(repository.acharPeloId(playlistId)).thenReturn(Optional.of(playlist));
        when(repository.existeMusicaNaPlaylist(playlistId, musicaId)).thenReturn(true);

        assertThrows(RuntimeException.class,
                () -> useCase.execute(playlistId, musicaId, donoId));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoForDono() {
        final PlaylistRepository repository = mock(PlaylistRepository.class);
        final AdicionarMusicaPlaylistUseCase useCase = new AdicionarMusicaPlaylistUseCase(repository);
        final UUID playlistId = UUID.randomUUID();
        final var playlist = new Playlist(playlistId, "Nome", null, Visibilidade.PUBLICA,
                UUID.randomUUID(), List.of(), List.of());

        when(repository.acharPeloId(playlistId)).thenReturn(Optional.of(playlist));

        assertThrows(RuntimeException.class,
                () -> useCase.execute(playlistId, UUID.randomUUID(), UUID.randomUUID()));
    }
}
