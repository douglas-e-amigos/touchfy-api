package com.ufrn.dct.bsi.touchfy.application.usecases.permission;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.PermissionMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.permission.PermissionResponse;
import com.ufrn.dct.bsi.touchfy.domain.permission.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ListarPermissionsUseCase {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public List<PermissionResponse> execute() {
        return permissionRepository.listarTodos().stream()
                .map(permissionMapper::toResponse)
                .toList();
    }
}
