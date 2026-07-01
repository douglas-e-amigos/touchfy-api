package com.ufrn.dct.bsi.touchfy.application.usecases.permission;

import com.ufrn.dct.bsi.touchfy.application.dtos.permission.UpdatePermissionRequest;
import com.ufrn.dct.bsi.touchfy.domain.permission.Permission;
import com.ufrn.dct.bsi.touchfy.domain.permission.repository.PermissionRepository;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AtualizarPermissionUseCase {

    private final PermissionRepository permissionRepository;

    public void execute(final Long id, final UpdatePermissionRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new IllegalArgumentException("O nome da permissão é obrigatório.");
        }
        final Permission permission = permissionRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Permissão não encontrada para o ID: " + id));

        permission.setName(request.name());
        permissionRepository.atualizar(permission);
    }
}
