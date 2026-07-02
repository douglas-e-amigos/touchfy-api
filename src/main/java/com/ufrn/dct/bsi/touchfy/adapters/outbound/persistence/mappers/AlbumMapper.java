package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumMusicaEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumSalvoEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.GeneroMusicalEntity;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumSalvoResponse;
import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import com.ufrn.dct.bsi.touchfy.domain.album.models.AlbumSalvo;
import com.ufrn.dct.bsi.touchfy.domain.album.models.TipoAlbum;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.GeneroMusical;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AlbumMapper {
  @Mapping(target = "musicas", source = "musicas")
  @Mapping(target = "generoMusical", source = "generoMusical")
  Album toDomain(AlbumEntity entity);

  @Mapping(target = "musicas", source = "musicas")
  @Mapping(target = "generoMusical", source = "generoMusical")
  @Mapping(target = "tipo", source = "tipo", qualifiedByName = "mapTipo")
  AlbumResponse toResponse(Album album);

  AlbumSalvo toSalvoDomain(AlbumSalvoEntity entity);

  AlbumSalvoResponse toSalvoResponse(AlbumSalvo albumSalvo);

  @Mapping(target = "id", source = "musica.id")
  @Mapping(target = "nome", source = "musica.nome")
  @Mapping(target = "caminhoDoArquivo", source = "musica.caminhoDoArquivo")
  @Mapping(target = "letra", source = "musica.letra")
  @Mapping(target = "tags", ignore = true)
  @Mapping(target = "generosMusicais", ignore = true)
  Musica toMusica(AlbumMusicaEntity entity);

  GeneroMusical toGeneroMusical(GeneroMusicalEntity entity);

  default List<AlbumSalvo> toSalvoDomainList(List<AlbumSalvoEntity> entities) {
    return entities.stream().map(this::toSalvoDomain).toList();
  }

  @Named("mapTipo")
  default TipoAlbum mapTipo(TipoAlbum tipo) {
    return tipo == null ? TipoAlbum.ALBUM : tipo;
  }

  default Album toAlbumDeSingleAvulso(final Musica musica) {
    return Album.builder()
        .id(null)
        .nome(musica.getNome())
        .descricao(null)
        .dataLancamento(null)
        .generoMusical(null)
        .artistaId(null)
        .tipo(TipoAlbum.SINGLE)
        .musicas(List.of(musica))
        .build();
  }
}
