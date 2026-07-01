package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.MusicaMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.MusicaResponse;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BuscarMusicaUseCase {
  private final MusicaRepository repository;
  private final MusicaMapper musicaMapper;

  public MusicaResponse execute(final UUID id) {
    final var musica =
        repository
            .acharPeloId(id)
            .orElseThrow(() -> new RuntimeException("Música não encontrada."));

    return musicaMapper.toResponse(musica);
  }
}
