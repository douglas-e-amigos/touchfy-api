package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.GeneroDaMusicaEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.MusicaDaTagEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.MusicaEntity;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.MusicaResponse;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.GeneroMusical;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MusicaMapper {
    // @Mapping(target = "id", source = "id")
    // @Mapping(target = "nome", source = "nome")
    // @Mapping(target = "caminhoDoArquivo", source = "caminhoDoArquivo")
    // @Mapping(target = "letra", source = "letra")
    // @Mapping(target = "tags", source = "tags")
    // @Mapping(target = "generosMusicais", source = "generosMusicais")
    Musica toDomain(MusicaEntity entity);

    // @Mapping(target = "id", source = "id")
    // @Mapping(target = "nome", source = "nome")
    // @Mapping(target = "caminhoDoArquivo", source = "caminhoDoArquivo")
    // @Mapping(target = "letra", source = "letra")
    // @Mapping(target = "tags", source = "tags")
    // @Mapping(target = "generosMusicais", source = "generosMusicais")
    MusicaResponse toResponse(Musica musica);

    @Mapping(target = "id", source = "tag.id")
    @Mapping(target = "nome", source = "tag.nome")
    Tag toTag(MusicaDaTagEntity entity);

    @Mapping(target = "id", source = "generoMusical.id")
    @Mapping(target = "nome", source = "generoMusical.nome")
    GeneroMusical toGeneroMusical(GeneroDaMusicaEntity entity);
}
