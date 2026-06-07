package com.ufrn.dct.bsi.touchfy.application.usecases.permission;

import com.ufrn.dct.bsi.touchfy.application.dtos.permission.CreatePermissionRequest;
import com.ufrn.dct.bsi.touchfy.domain.permission.Permission;
import com.ufrn.dct.bsi.touchfy.domain.permission.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CriarPermissionUseCase {

    private final PermissionRepository permissionRepository;

    public void execute(final CreatePermissionRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("O nome da permissão é obrigatório.");
        }
        final Permission permission = new Permission();
        permission.setName(request.name());
        permissionRepository.salvar(permission);
    }
}
