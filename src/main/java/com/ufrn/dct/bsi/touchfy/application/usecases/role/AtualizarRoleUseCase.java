package com.ufrn.dct.bsi.touchfy.application.usecases.role;

import com.ufrn.dct.bsi.touchfy.application.dtos.role.UpdateRoleRequest;
import com.ufrn.dct.bsi.touchfy.domain.permission.Permission;
import com.ufrn.dct.bsi.touchfy.domain.permission.repository.PermissionRepository;
import com.ufrn.dct.bsi.touchfy.domain.role.Role;
import com.ufrn.dct.bsi.touchfy.domain.role.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class AtualizarRoleUseCase {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public void execute(final Long id, final UpdateRoleRequest request) {
        if (request.name() == null) {
            throw new IllegalArgumentException("O nome do perfil é obrigatório.");
        }
        final Role role = roleRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado para o ID: " + id));

        final Set<Permission> permissions = new HashSet<>();
        if (request.permissionIds() != null) {
            for (final Long permissionId : request.permissionIds()) {
                final Permission permission = permissionRepository.buscarPorId(permissionId)
                        .orElseThrow(() -> new RuntimeException("Permissão não encontrada para o ID: " + permissionId));
                permissions.add(permission);
            }
        }

        role.setName(request.name());
        role.setPermissions(permissions);

        roleRepository.atualizar(role);
    }
}
