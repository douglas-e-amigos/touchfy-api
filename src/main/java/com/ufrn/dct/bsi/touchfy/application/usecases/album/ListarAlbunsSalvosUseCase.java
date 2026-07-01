package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.AlbumMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumSalvoResponse;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class ListarAlbunsSalvosUseCase {
    private final AlbumRepository repository;
    private final AlbumMapper albumMapper;

    public List<AlbumSalvoResponse> execute(final UUID usuarioId) {
        return repository.listarSalvos(usuarioId).stream()
                .map(albumMapper::toSalvoResponse)
                .toList();
    }
}
