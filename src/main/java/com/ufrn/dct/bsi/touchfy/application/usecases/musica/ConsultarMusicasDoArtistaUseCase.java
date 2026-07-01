package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.MusicaMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.MusicaResponse;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConsultarMusicasDoArtistaUseCase {
  private final MusicaRepository repository;
  private final MusicaMapper musicaMapper;

  public List<MusicaResponse> execute(final UUID artistaId) {
    if (artistaId == null) {
      throw new IllegalArgumentException("ID do artista é obrigatório.");
    }

    return repository.consultarPorCriadoPor(artistaId).stream()
        .map(musicaMapper::toResponse)
        .toList();
  }
}
