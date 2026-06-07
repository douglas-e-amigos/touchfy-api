package com.ufrn.dct.bsi.touchfy.application.usecases.permission;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.PermissionMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.permission.PermissionResponse;
import com.ufrn.dct.bsi.touchfy.domain.permission.Permission;
import com.ufrn.dct.bsi.touchfy.domain.permission.repository.PermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BuscarPermissionPorIdUseCaseTest {

    private PermissionRepository permissionRepository;
    private PermissionMapper permissionMapper;
    private BuscarPermissionPorIdUseCase useCase;

    @BeforeEach
    void setup() {
        permissionRepository = mock(PermissionRepository.class);
        permissionMapper = mock(PermissionMapper.class);
        useCase = new BuscarPermissionPorIdUseCase(permissionRepository, permissionMapper);
    }

    private Permission permissionMock() {
        return new Permission(1L, "READ");
    }

    private PermissionResponse responseMock() {
        return new PermissionResponse(1L, "READ");
    }

    @Test
    void deveRetornarPermissionQuandoEncontrada() {
        final Permission permission = permissionMock();
        final PermissionResponse response = responseMock();

        when(permissionRepository.buscarPorId(1L)).thenReturn(Optional.of(permission));
        when(permissionMapper.toResponse(permission)).thenReturn(response);

        final PermissionResponse result = useCase.execute(1L);

        assertEquals(response, result);
        verify(permissionRepository).buscarPorId(1L);
        verify(permissionMapper).toResponse(permission);
    }

    @Test
    void deveLancarExcecaoQuandoPermissionNaoForEncontrada() {
        when(permissionRepository.buscarPorId(1L)).thenReturn(Optional.empty());

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> useCase.execute(1L)
        );

        assertEquals("Permissão não encontrada para o ID: 1", exception.getMessage());
        verify(permissionRepository).buscarPorId(1L);
        verifyNoInteractions(permissionMapper);
    }
}
