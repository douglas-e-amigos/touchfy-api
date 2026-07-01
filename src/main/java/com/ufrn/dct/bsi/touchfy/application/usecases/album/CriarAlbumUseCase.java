package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import com.ufrn.dct.bsi.touchfy.application.dtos.album.CriarAlbumRequest;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CriarAlbumUseCase {
    private final AlbumRepository repository;

    public void execute(final CriarAlbumRequest request, final UUID usuarioId) {
        if (request == null || request.nome() == null || request.nome().isBlank()) {
            throw new IllegalArgumentException("Os dados do álbum são obrigatórios.");
        }

        repository.criar(request, usuarioId);
    }
}
