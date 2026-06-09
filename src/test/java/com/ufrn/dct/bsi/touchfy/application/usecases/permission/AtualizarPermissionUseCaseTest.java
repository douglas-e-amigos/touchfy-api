package com.ufrn.dct.bsi.touchfy.application.usecases.permission;

import com.ufrn.dct.bsi.touchfy.application.dtos.permission.UpdatePermissionRequest;
import com.ufrn.dct.bsi.touchfy.domain.permission.Permission;
import com.ufrn.dct.bsi.touchfy.domain.permission.repository.PermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AtualizarPermissionUseCaseTest {

    private PermissionRepository permissionRepository;
    private AtualizarPermissionUseCase useCase;

    @BeforeEach
    void setup() {
        permissionRepository = mock(PermissionRepository.class);
        useCase = new AtualizarPermissionUseCase(permissionRepository);
    }

    private Permission permissionMock() {
        return new Permission(1L, "READ");
    }

    private UpdatePermissionRequest requestValido() {
        return new UpdatePermissionRequest("WRITE");
    }

    @Test
    void deveAtualizarPermissionComSucesso() {
        final Permission permission = permissionMock();
        final UpdatePermissionRequest request = requestValido();

        when(permissionRepository.buscarPorId(1L)).thenReturn(Optional.of(permission));

        useCase.execute(1L, request);

        final ArgumentCaptor<Permission> captor = ArgumentCaptor.forClass(Permission.class);
        verify(permissionRepository).atualizar(captor.capture());

        final Permission permissionAtualizada = captor.getValue();
        assertEquals(1L, permissionAtualizada.getId());
        assertEquals("WRITE", permissionAtualizada.getName());
    }

    @Test
    void deveLancarExcecaoQuandoNameForNulo() {
        final UpdatePermissionRequest request = new UpdatePermissionRequest(null);

        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(1L, request)
        );

        assertEquals("O nome da permissão é obrigatório.", exception.getMessage());
        verifyNoInteractions(permissionRepository);
    }

    @Test
    void deveLancarExcecaoQuandoNameForVazio() {
        final UpdatePermissionRequest request = new UpdatePermissionRequest("   ");

        final IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> useCase.execute(1L, request)
        );

        assertEquals("O nome da permissão é obrigatório.", exception.getMessage());
        verifyNoInteractions(permissionRepository);
    }

    @Test
    void deveLancarExcecaoQuandoPermissionNaoExistir() {
        final UpdatePermissionRequest request = requestValido();

        when(permissionRepository.buscarPorId(1L)).thenReturn(Optional.empty());

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> useCase.execute(1L, request)
        );

        assertEquals("Permissão não encontrada para o ID: 1", exception.getMessage());
        verify(permissionRepository, never()).atualizar(any());
    }
}
