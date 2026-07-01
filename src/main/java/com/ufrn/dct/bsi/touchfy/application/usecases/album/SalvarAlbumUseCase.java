package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.ConflitoDeNegocioException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class SalvarAlbumUseCase {
    private final AlbumRepository repository;

    public void execute(final UUID albumId, final UUID usuarioId) {
        if (repository.acharPeloId(albumId).isEmpty()) {
            throw new RecursoNaoEncontradoException("Álbum não encontrado.");
        }

        if (repository.existeAlbumSalvo(albumId, usuarioId)) {
            throw new ConflitoDeNegocioException("Álbum já está salvo.");
        }

        repository.salvar(albumId, usuarioId);
    }
}
