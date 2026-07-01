package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.AcessoNegadoException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.ConflitoDeNegocioException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AdicionarMusicaAlbumUseCase {
  private final AlbumRepository repository;

  public void execute(final UUID albumId, final UUID musicaId, final UUID usuarioId) {
    final var album =
        repository
            .acharPeloId(albumId)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Álbum não encontrado."));

    if (!album.getArtistaId().equals(usuarioId)) {
      throw new AcessoNegadoException("Usuário não autorizado a modificar este álbum.");
    }

    if (repository.existeMusicaNoAlbum(albumId, musicaId)) {
      throw new ConflitoDeNegocioException("Música já está no álbum.");
    }

    final int proximaOrdem = album.getMusicas().size();
    repository.adicionarMusica(albumId, musicaId, proximaOrdem);
  }
}
