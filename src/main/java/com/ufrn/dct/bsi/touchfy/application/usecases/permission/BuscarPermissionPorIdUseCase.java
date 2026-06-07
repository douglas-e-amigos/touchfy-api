package com.ufrn.dct.bsi.touchfy.application.usecases.permission;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.PermissionMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.permission.PermissionResponse;
import com.ufrn.dct.bsi.touchfy.domain.permission.Permission;
import com.ufrn.dct.bsi.touchfy.domain.permission.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BuscarPermissionPorIdUseCase {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionResponse execute(final Long id) {
        final Permission permission = permissionRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Permissão não encontrada para o ID: " + id));
        return permissionMapper.toResponse(permission);
    }
}
