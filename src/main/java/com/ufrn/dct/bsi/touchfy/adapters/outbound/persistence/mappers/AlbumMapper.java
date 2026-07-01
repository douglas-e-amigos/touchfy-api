package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumMusicaEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumSalvoEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.GeneroMusicalEntity;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumSalvoResponse;
import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import com.ufrn.dct.bsi.touchfy.domain.album.models.AlbumSalvo;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.GeneroMusical;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AlbumMapper {
  @Mapping(target = "musicas", source = "musicas")
  @Mapping(target = "generoMusical", source = "generoMusical")
  Album toDomain(AlbumEntity entity);

  @Mapping(target = "musicas", source = "musicas")
  @Mapping(target = "generoMusical", source = "generoMusical")
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
}
