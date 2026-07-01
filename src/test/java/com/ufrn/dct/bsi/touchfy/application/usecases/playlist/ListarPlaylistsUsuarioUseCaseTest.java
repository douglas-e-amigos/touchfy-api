package com.ufrn.dct.bsi.touchfy.application.usecases.playlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.PlaylistMapper;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Playlist;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Visibilidade;
import com.ufrn.dct.bsi.touchfy.domain.playlist.repositories.PlaylistRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ListarPlaylistsUsuarioUseCaseTest {

  @Test
  void deveListarPlaylistsVisiveisParaUsuario() {
    final PlaylistRepository repository = mock(PlaylistRepository.class);
    final PlaylistMapper mapper = mock(PlaylistMapper.class);
    final ListarPlaylistsUsuarioUseCase useCase =
        new ListarPlaylistsUsuarioUseCase(repository, mapper);
    final UUID usuarioId = UUID.randomUUID();
    final var playlist =
        new Playlist(
            UUID.randomUUID(),
            "Nome",
            null,
            Visibilidade.PUBLICA,
            UUID.randomUUID(),
            List.of(),
            List.of());

    when(repository.listarVisiveisParaUsuario(usuarioId)).thenReturn(List.of(playlist));

    final var resultado = useCase.execute(usuarioId);

    assertEquals(1, resultado.size());
  }
}
