package com.ufrn.dct.bsi.touchfy.application.usecases.playlist;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.AtualizarPlaylistRequest;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Playlist;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Visibilidade;
import com.ufrn.dct.bsi.touchfy.domain.playlist.repositories.PlaylistRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AtualizarPlaylistUseCaseTest {

  @Test
  void deveAtualizarPlaylistQuandoUsuarioForDono() {
    final PlaylistRepository repository = mock(PlaylistRepository.class);
    final AtualizarPlaylistUseCase useCase = new AtualizarPlaylistUseCase(repository);
    final UUID playlistId = UUID.randomUUID();
    final UUID donoId = UUID.randomUUID();
    final var playlist =
        new Playlist(playlistId, "Nome", null, Visibilidade.PUBLICA, donoId, List.of(), List.of());
    final var request =
        new AtualizarPlaylistRequest("Novo Nome", "Nova Desc", Visibilidade.PRIVADA, List.of());

    when(repository.acharPeloId(playlistId)).thenReturn(Optional.of(playlist));

    useCase.execute(playlistId, request, donoId);

    verify(repository).atualizar(playlistId, request);
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoForDono() {
    final PlaylistRepository repository = mock(PlaylistRepository.class);
    final AtualizarPlaylistUseCase useCase = new AtualizarPlaylistUseCase(repository);
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
    final var request =
        new AtualizarPlaylistRequest("Novo Nome", null, Visibilidade.PUBLICA, List.of());

    when(repository.acharPeloId(playlistId)).thenReturn(Optional.of(playlist));

    assertThrows(
        RuntimeException.class, () -> useCase.execute(playlistId, request, UUID.randomUUID()));
  }

  @Test
  void deveLancarExcecaoQuandoPlaylistNaoExistir() {
    final PlaylistRepository repository = mock(PlaylistRepository.class);
    final AtualizarPlaylistUseCase useCase = new AtualizarPlaylistUseCase(repository);
    final var request = new AtualizarPlaylistRequest("Nome", null, Visibilidade.PUBLICA, List.of());

    when(repository.acharPeloId(any())).thenReturn(Optional.empty());

    assertThrows(
        RuntimeException.class,
        () -> useCase.execute(UUID.randomUUID(), request, UUID.randomUUID()));
  }
}
