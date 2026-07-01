package com.ufrn.dct.bsi.touchfy.application.usecases.playlist;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Playlist;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Visibilidade;
import com.ufrn.dct.bsi.touchfy.domain.playlist.repositories.PlaylistRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class RemoverMusicaPlaylistUseCaseTest {

  @Test
  void deveRemoverMusicaQuandoUsuarioForDono() {
    final PlaylistRepository repository = mock(PlaylistRepository.class);
    final RemoverMusicaPlaylistUseCase useCase = new RemoverMusicaPlaylistUseCase(repository);
    final UUID playlistId = UUID.randomUUID();
    final UUID musicaId = UUID.randomUUID();
    final UUID donoId = UUID.randomUUID();
    final var playlist =
        new Playlist(playlistId, "Nome", null, Visibilidade.PUBLICA, donoId, List.of(), List.of());

    when(repository.acharPeloId(playlistId)).thenReturn(Optional.of(playlist));

    useCase.execute(playlistId, musicaId, donoId);

    verify(repository).removerMusica(playlistId, musicaId);
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoForDono() {
    final PlaylistRepository repository = mock(PlaylistRepository.class);
    final RemoverMusicaPlaylistUseCase useCase = new RemoverMusicaPlaylistUseCase(repository);
    final UUID playlistId = UUID.randomUUID();
    final var playlist =
        new Playlist(
            playlistId,
            "Nome",
            null,
            Visibilidade.PUBLICA,
            UUID.randomUUID(),
            List.of(),
            List.of());

    when(repository.acharPeloId(playlistId)).thenReturn(Optional.of(playlist));

    assertThrows(
        RuntimeException.class,
        () -> useCase.execute(playlistId, UUID.randomUUID(), UUID.randomUUID()));
  }
}
