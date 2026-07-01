package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.AcessoNegadoException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class DeletarAlbumUseCase {
    private final AlbumRepository repository;

    public void execute(final UUID albumId, final UUID usuarioId) {
        final var album = repository.acharPeloId(albumId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Álbum não encontrado."));

        if (!album.getArtistaId().equals(usuarioId)) {
            throw new AcessoNegadoException("Usuário não autorizado a excluir este álbum.");
        }

        repository.deletar(albumId);
    }
}
