package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class AdicionarMusicaAlbumUseCase {
    private final AlbumRepository repository;

    public void execute(final UUID albumId, final UUID musicaId, final UUID usuarioId) {
        final var album = repository.acharPeloId(albumId)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado."));

        if (!album.getArtistaId().equals(usuarioId)) {
            throw new RuntimeException("Usuário não autorizado a modificar este álbum.");
        }

        if (repository.existeMusicaNoAlbum(albumId, musicaId)) {
            throw new RuntimeException("Música já está no álbum.");
        }

        final int proximaOrdem = album.getMusicas().size();
        repository.adicionarMusica(albumId, musicaId, proximaOrdem);
    }
}
