package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.GeneroMusicalEntity;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.GeneroMusical;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GeneroMusicalMapper {
    GeneroMusical toDomain(GeneroMusicalEntity entity);

    GeneroMusicalEntity toEntity(GeneroMusical domain);
}