package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.RoleEntity;
import com.ufrn.dct.bsi.touchfy.application.dtos.role.RoleResponse;
import com.ufrn.dct.bsi.touchfy.domain.role.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    Role toDomain(RoleEntity entity);
    RoleEntity toEntity(Role domain);
    RoleResponse toResponse(Role domain);
}
