package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.PlaylistEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.PlaylistMusicaEntity;
import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.PlaylistResponse;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Playlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {
    @Mapping(target = "musicas", source = "musicas")
    @Mapping(target = "usuariosConvidadosIds", expression = "java(toConvidadosIds(entity))")
    Playlist toDomain(PlaylistEntity entity);

    @Mapping(target = "musicas", source = "musicas")
    PlaylistResponse toResponse(Playlist playlist);

    @Mapping(target = "id", source = "musica.id")
    @Mapping(target = "nome", source = "musica.nome")
    @Mapping(target = "caminhoDoArquivo", source = "musica.caminhoDoArquivo")
    @Mapping(target = "letra", source = "musica.letra")
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "generosMusicais", ignore = true)
    Musica toMusica(PlaylistMusicaEntity entity);

    default List<UUID> toConvidadosIds(PlaylistEntity entity) {
        if (entity.getUsuariosConvidados() == null) {
            return List.of();
        }
        return entity.getUsuariosConvidados().stream()
                .map(u -> u.getId())
                .toList();
    }
}
