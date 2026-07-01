package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.AlbumMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumResponse;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BuscarAlbumUseCase {
  private final AlbumRepository repository;
  private final AlbumMapper albumMapper;

  public AlbumResponse execute(final UUID albumId) {
    final var album =
        repository
            .acharPeloId(albumId)
            .orElseThrow(() -> new RuntimeException("Álbum não encontrado."));

    return albumMapper.toResponse(album);
  }
}
