package com.ufrn.dct.bsi.touchfy.application.usecases.playlist;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.PlaylistMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.PlaylistResponse;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Playlist;
import com.ufrn.dct.bsi.touchfy.domain.playlist.repositories.PlaylistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class BuscarPlaylistUseCase {
    private final PlaylistRepository repository;
    private final PlaylistMapper playlistMapper;

    public PlaylistResponse execute(final UUID playlistId, final UUID usuarioId) {
        final var playlist = repository.acharPeloId(playlistId)
                .orElseThrow(() -> new RuntimeException("Playlist não encontrada."));

        if (!isVisivelPara(playlist, usuarioId)) {
            throw new RuntimeException("Playlist não encontrada.");
        }

        return playlistMapper.toResponse(playlist);
    }

    private boolean isVisivelPara(final Playlist playlist, final UUID usuarioId) {
        return switch (playlist.getVisibilidade()) {
            case PUBLICA -> true;
            case PRIVADA -> playlist.getDonoId().equals(usuarioId);
            case PROTEGIDA -> playlist.getDonoId().equals(usuarioId)
                    || playlist.getUsuariosConvidadosIds().contains(usuarioId);
        };
    }
}
