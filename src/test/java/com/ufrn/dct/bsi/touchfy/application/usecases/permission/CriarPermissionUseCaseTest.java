package com.ufrn.dct.bsi.touchfy.application.usecases.permission;

import com.ufrn.dct.bsi.touchfy.application.dtos.permission.CreatePermissionRequest;
import com.ufrn.dct.bsi.touchfy.domain.permission.Permission;
import com.ufrn.dct.bsi.touchfy.domain.permission.repository.PermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CriarPermissionUseCaseTest {

    private PermissionRepository permissionRepository;
    private CriarPermissionUseCase useCase;

    @BeforeEach
    void setup() {
        permissionRepository = mock(PermissionRepository.class);
        useCase = new CriarPermissionUseCase(permissionRepository);
    }

    private CreatePermissionRequest requestValido() {
        return new CreatePermissionRequest("READ_PERMISSIONS");
    }

    @Test
    void deveCriarPermissionComSucesso() {
        final CreatePermissionRequest request = requestValido();

        useCase.execute(request);

        final ArgumentCaptor<Permission> captor = ArgumentCaptor.forClass(Permission.class);
        verify(permissionRepository).salvar(captor.capture());

        final Permission permissionSalva = captor.getValue();
        assertEquals("READ_PERMISSIONS", permissionSalva.getName());
    }

    @Test
    void deveLancarExcecaoQuandoNameForNulo() {
        final CreatePermissionRequest request = new CreatePermissionRequest(null);

        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(request)
        );

        assertEquals("O nome da permissão é obrigatório.", exception.getMessage());
        verifyNoInteractions(permissionRepository);
    }

    @Test
    void deveLancarExcecaoQuandoNameForVazio() {
        final CreatePermissionRequest request = new CreatePermissionRequest("   ");

        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(request)
        );

        assertEquals("O nome da permissão é obrigatório.", exception.getMessage());
        verifyNoInteractions(permissionRepository);
    }
}
