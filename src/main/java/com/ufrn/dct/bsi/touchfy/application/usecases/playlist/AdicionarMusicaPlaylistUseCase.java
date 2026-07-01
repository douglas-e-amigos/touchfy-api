package com.ufrn.dct.bsi.touchfy.application.usecases.playlist;

import com.ufrn.dct.bsi.touchfy.domain.playlist.repositories.PlaylistRepository;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.AcessoNegadoException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.ConflitoDeNegocioException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class AdicionarMusicaPlaylistUseCase {
    private final PlaylistRepository repository;

    public void execute(final UUID playlistId, final UUID musicaId, final UUID usuarioId) {
        final var playlist = repository.acharPeloId(playlistId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Playlist não encontrada."));

        if (!playlist.getDonoId().equals(usuarioId)) {
            throw new AcessoNegadoException("Usuário não autorizado a modificar esta playlist.");
        }

        if (repository.existeMusicaNaPlaylist(playlistId, musicaId)) {
            throw new ConflitoDeNegocioException("Música já está na playlist.");
        }

        final int proximaOrdem = playlist.getMusicas().size();
        repository.adicionarMusica(playlistId, musicaId, proximaOrdem);
    }
}
