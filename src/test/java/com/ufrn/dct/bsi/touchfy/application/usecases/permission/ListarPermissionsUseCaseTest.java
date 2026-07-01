package com.ufrn.dct.bsi.touchfy.application.usecases.permission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.PermissionMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.permission.PermissionResponse;
import com.ufrn.dct.bsi.touchfy.domain.permission.Permission;
import com.ufrn.dct.bsi.touchfy.domain.permission.repository.PermissionRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListarPermissionsUseCaseTest {

  private PermissionRepository permissionRepository;
  private PermissionMapper permissionMapper;
  private ListarPermissionsUseCase useCase;

  @BeforeEach
  void setup() {
    permissionRepository = mock(PermissionRepository.class);
    permissionMapper = mock(PermissionMapper.class);
    useCase = new ListarPermissionsUseCase(permissionRepository, permissionMapper);
  }

  private Permission permissionMock(final Long id, final String name) {
    return new Permission(id, name);
  }

  private PermissionResponse responseMock(final Long id, final String name) {
    return new PermissionResponse(id, name);
  }

  @Test
  void deveRetornarListaDePermissions() {
    final Permission p1 = permissionMock(1L, "READ");
    final Permission p2 = permissionMock(2L, "WRITE");
    final PermissionResponse res1 = responseMock(1L, "READ");
    final PermissionResponse res2 = responseMock(2L, "WRITE");

    when(permissionRepository.listarTodos()).thenReturn(List.of(p1, p2));
    when(permissionMapper.toResponse(p1)).thenReturn(res1);
    when(permissionMapper.toResponse(p2)).thenReturn(res2);

    final List<PermissionResponse> result = useCase.execute();

    assertEquals(2, result.size());
    assertEquals(res1, result.get(0));
    assertEquals(res2, result.get(1));
    verify(permissionRepository).listarTodos();
    verify(permissionMapper).toResponse(p1);
    verify(permissionMapper).toResponse(p2);
  }

  @Test
  void deveRetornarListaVaziaQuandoNaoHouverPermissions() {
    when(permissionRepository.listarTodos()).thenReturn(List.of());

    final List<PermissionResponse> result = useCase.execute();

    assertTrue(result.isEmpty());
    verify(permissionRepository).listarTodos();
    verifyNoInteractions(permissionMapper);
  }
}
