package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.MusicaMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.MusicaResponse;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConsultarMusicasUseCase {
  private final MusicaRepository repository;
  private final MusicaMapper musicaMapper;

  public List<MusicaResponse> execute() {
    return execute(null);
  }

  public List<MusicaResponse> execute(final String artista) {
    final List<Musica> musicas =
        artista == null || artista.isBlank()
            ? repository.consultar()
            : repository.consultarPorNomeArtista(artista.trim());

    return musicas.stream().map(musicaMapper::toResponse).toList();
  }
}
