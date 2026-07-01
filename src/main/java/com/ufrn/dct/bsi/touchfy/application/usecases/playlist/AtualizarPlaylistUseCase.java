package com.ufrn.dct.bsi.touchfy.application.usecases.playlist;

import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.AtualizarPlaylistRequest;
import com.ufrn.dct.bsi.touchfy.domain.playlist.repositories.PlaylistRepository;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.AcessoNegadoException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class AtualizarPlaylistUseCase {
    private final PlaylistRepository repository;

    public void execute(final UUID playlistId, final AtualizarPlaylistRequest request, final UUID usuarioId) {
        final var playlist = repository.acharPeloId(playlistId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Playlist não encontrada."));

        if (!playlist.getDonoId().equals(usuarioId)) {
            throw new AcessoNegadoException("Usuário não autorizado a editar esta playlist.");
        }

        repository.atualizar(playlistId, request);
    }
}
