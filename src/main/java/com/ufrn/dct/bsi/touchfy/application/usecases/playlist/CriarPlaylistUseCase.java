package com.ufrn.dct.bsi.touchfy.application.usecases.playlist;

import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.CriarPlaylistRequest;
import com.ufrn.dct.bsi.touchfy.domain.playlist.repositories.PlaylistRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CriarPlaylistUseCase {
    private final PlaylistRepository repository;

    public void execute(final CriarPlaylistRequest request, final UUID usuarioId) {
        if (request == null || request.nome() == null || request.nome().isBlank()) {
            throw new IllegalArgumentException("Os dados da playlist são obrigatórios.");
        }

        repository.criar(UUID.randomUUID(), request, usuarioId);
    }
}
