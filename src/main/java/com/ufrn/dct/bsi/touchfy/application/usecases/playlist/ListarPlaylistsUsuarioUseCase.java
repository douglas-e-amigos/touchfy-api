package com.ufrn.dct.bsi.touchfy.application.usecases.playlist;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.PlaylistMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.PlaylistResponse;
import com.ufrn.dct.bsi.touchfy.domain.playlist.repositories.PlaylistRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ListarPlaylistsUsuarioUseCase {
  private final PlaylistRepository repository;
  private final PlaylistMapper playlistMapper;

  public List<PlaylistResponse> execute(final UUID usuarioId) {
    return repository.listarVisiveisParaUsuario(usuarioId).stream()
        .map(playlistMapper::toResponse)
        .toList();
  }
}
