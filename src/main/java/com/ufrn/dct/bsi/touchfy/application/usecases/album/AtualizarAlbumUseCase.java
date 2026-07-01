package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import com.ufrn.dct.bsi.touchfy.application.dtos.album.AtualizarAlbumRequest;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.AcessoNegadoException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AtualizarAlbumUseCase {
  private final AlbumRepository repository;

  public void execute(
      final UUID albumId, final AtualizarAlbumRequest request, final UUID usuarioId) {
    final var album =
        repository
            .acharPeloId(albumId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Álbum não encontrado."));

    if (!album.getArtistaId().equals(usuarioId)) {
      throw new AcessoNegadoException("Usuário não autorizado a editar este álbum.");
    }

    repository.atualizar(albumId, request);
  }
}
