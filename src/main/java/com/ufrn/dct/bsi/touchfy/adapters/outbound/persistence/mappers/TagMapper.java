package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.TagEntity;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Tag;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TagMapper {
  Tag toDomain(TagEntity entity);

  TagEntity toEntity(Tag domain);
}
