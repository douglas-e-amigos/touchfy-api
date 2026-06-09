package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.PermissionEntity;
import com.ufrn.dct.bsi.touchfy.application.dtos.permission.PermissionResponse;
import com.ufrn.dct.bsi.touchfy.domain.permission.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toDomain(PermissionEntity entity);
    PermissionEntity toEntity(Permission domain);
    PermissionResponse toResponse(Permission domain);
}
