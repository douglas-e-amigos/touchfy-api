package com.ufrn.dct.bsi.touchfy.application.usecases.playlist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.PlaylistMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.PlaylistResponse;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Playlist;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Visibilidade;
import com.ufrn.dct.bsi.touchfy.domain.playlist.repositories.PlaylistRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BuscarPlaylistUseCaseTest {

  @Test
  void deveRetornarPlaylistQuandoUsuarioForDonoDePlaylistPrivada() {
    final PlaylistRepository repository = mock(PlaylistRepository.class);
    final PlaylistMapper mapper = mock(PlaylistMapper.class);
    final BuscarPlaylistUseCase useCase = new BuscarPlaylistUseCase(repository, mapper);
    final UUID playlistId = UUID.randomUUID();
    final UUID donoId = UUID.randomUUID();
    final var playlist =
        new Playlist(playlistId, "Nome", null, Visibilidade.PRIVADA, donoId, List.of(), List.of());
    final var response =
        new PlaylistResponse(
            playlistId, "Nome", null, Visibilidade.PRIVADA, donoId, List.of(), null, null);

    when(repository.acharPeloId(playlistId)).thenReturn(Optional.of(playlist));
    when(mapper.toResponse(playlist)).thenReturn(response);

    final var resultado = useCase.execute(playlistId, donoId);

    assertEquals("Nome", resultado.nome());
  }

  @Test
  void devePermitirVisualizarPlaylistPublicaParaQualquerUsuario() {
    final PlaylistRepository repository = mock(PlaylistRepository.class);
    final PlaylistMapper mapper = mock(PlaylistMapper.class);
    final BuscarPlaylistUseCase useCase = new BuscarPlaylistUseCase(repository, mapper);
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
    when(mapper.toResponse(any())).thenReturn(mock(PlaylistResponse.class));

    assertDoesNotThrow(() -> useCase.execute(playlistId, UUID.randomUUID()));
  }

  @Test
  void deveLancarExcecaoQuandoPlaylistPrivadaForAcessadaPorOutroUsuario() {
    final PlaylistRepository repository = mock(PlaylistRepository.class);
    final PlaylistMapper mapper = mock(PlaylistMapper.class);
    final BuscarPlaylistUseCase useCase = new BuscarPlaylistUseCase(repository, mapper);
    final UUID playlistId = UUID.randomUUID();
    final var playlist =
        new Playlist(
            playlistId,
            "Nome",
            null,
            Visibilidade.PRIVADA,
            UUID.randomUUID(),
            List.of(),
            List.of());

    when(repository.acharPeloId(playlistId)).thenReturn(Optional.of(playlist));

    assertThrows(RuntimeException.class, () -> useCase.execute(playlistId, UUID.randomUUID()));
  }

  @Test
  void devePermitirVisualizarPlaylistProtegidaQuandoUsuarioForConvidado() {
    final PlaylistRepository repository = mock(PlaylistRepository.class);
    final PlaylistMapper mapper = mock(PlaylistMapper.class);
    final BuscarPlaylistUseCase useCase = new BuscarPlaylistUseCase(repository, mapper);
    final UUID playlistId = UUID.randomUUID();
    final UUID convidadoId = UUID.randomUUID();
    final var playlist =
        new Playlist(
            playlistId,
            "Nome",
            null,
            Visibilidade.PROTEGIDA,
            UUID.randomUUID(),
            List.of(),
            List.of(convidadoId));

    when(repository.acharPeloId(playlistId)).thenReturn(Optional.of(playlist));
    when(mapper.toResponse(any())).thenReturn(mock(PlaylistResponse.class));

    assertDoesNotThrow(() -> useCase.execute(playlistId, convidadoId));
  }

  @Test
  void deveLancarExcecaoQuandoPlaylistNaoExistir() {
    final PlaylistRepository repository = mock(PlaylistRepository.class);
    final PlaylistMapper mapper = mock(PlaylistMapper.class);
    final BuscarPlaylistUseCase useCase = new BuscarPlaylistUseCase(repository, mapper);

    when(repository.acharPeloId(any())).thenReturn(Optional.empty());

    assertThrows(
        RuntimeException.class, () -> useCase.execute(UUID.randomUUID(), UUID.randomUUID()));
  }
}
