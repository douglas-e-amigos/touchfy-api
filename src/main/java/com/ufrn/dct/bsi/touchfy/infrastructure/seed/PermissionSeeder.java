package com.ufrn.dct.bsi.touchfy.infrastructure.seed;

import com.ufrn.dct.bsi.touchfy.application.dtos.permission.CreatePermissionRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.permission.CriarPermissionUseCase;
import com.ufrn.dct.bsi.touchfy.domain.permission.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Order(1)
@Component
@RequiredArgsConstructor
public class PermissionSeeder implements ApplicationRunner {
    private final CriarPermissionUseCase criarPermissionUseCase;
    private final PermissionRepository permissionRepository;

    private final static List<String> PERMISSIONS = List.of(
            "music:create",
            "music:read",
            "music:update",
            "music:delete",
            "file:upload",
            "file:read",
            "file:delete",
            "permission:create",
            "permission:read",
            "permission:patch",
            "permission:delete",
            "role:create",
            "role:read",
            "role:patch",
            "role:delete"
    );

    @Override
    public void run(final ApplicationArguments args) {
        PERMISSIONS.forEach(this::criaPermissaoSeNaoExistir);
    }

    public void criaPermissaoSeNaoExistir(final String nome) {
        if (!permissionRepository.existePorNome(nome)) {
            criarPermissionUseCase.execute(
                    new CreatePermissionRequest(nome)
            );
        }
    }
}
