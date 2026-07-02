package com.ufrn.dct.bsi.touchfy.application.usecases.artista;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.AlbumMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.artista.ArtistaDiscografiaResponse;
import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import com.ufrn.dct.bsi.touchfy.domain.album.models.TipoAlbum;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConsultarDiscografiaArtistaUseCase {
  private final AlbumRepository albumRepository;
  private final MusicaRepository musicaRepository;
  private final AlbumMapper albumMapper;

  public ArtistaDiscografiaResponse execute(final UUID artistaId) {
    final List<Album> albunsDoArtista = albumRepository.listarPorArtista(artistaId);

    final Map<TipoAlbum, List<Album>> porTipo =
        albunsDoArtista.stream().collect(Collectors.groupingBy(Album::getTipo));

    final Set<UUID> musicasEmAlbum =
        albunsDoArtista.stream()
            .flatMap(a -> a.getMusicas().stream())
            .map(Musica::getId)
            .collect(Collectors.toSet());

    final List<Album> singlesAvulsos =
        musicaRepository.consultarPorCriadoPor(artistaId).stream()
            .filter(m -> !musicasEmAlbum.contains(m.getId()))
            .map(albumMapper::toAlbumDeSingleAvulso)
            .toList();

    final List<AlbumResponse> singles =
        Stream.concat(
                porTipo.getOrDefault(TipoAlbum.SINGLE, List.of()).stream()
                    .map(albumMapper::toResponse),
                singlesAvulsos.stream().map(albumMapper::toResponse))
            .toList();

    return ArtistaDiscografiaResponse.builder()
        .albuns(mapOrEmpty(porTipo, TipoAlbum.ALBUM))
        .singles(singles)
        .eps(mapOrEmpty(porTipo, TipoAlbum.EP))
        .compilacoes(mapOrEmpty(porTipo, TipoAlbum.COMPILACAO))
        .build();
  }

  private List<AlbumResponse> mapOrEmpty(
      final Map<TipoAlbum, List<Album>> porTipo, final TipoAlbum tipo) {
    return porTipo.getOrDefault(tipo, List.of()).stream().map(albumMapper::toResponse).toList();
  }
}
