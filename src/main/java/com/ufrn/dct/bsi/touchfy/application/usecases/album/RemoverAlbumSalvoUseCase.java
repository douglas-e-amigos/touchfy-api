package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class RemoverAlbumSalvoUseCase {
    private final AlbumRepository repository;

    public void execute(final UUID albumId, final UUID usuarioId) {
        repository.removerSalvo(albumId, usuarioId);
    }
}
