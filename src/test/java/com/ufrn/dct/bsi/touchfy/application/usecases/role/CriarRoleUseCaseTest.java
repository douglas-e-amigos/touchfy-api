package com.ufrn.dct.bsi.touchfy.application.usecases.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.application.dtos.role.CreateRoleRequest;
import com.ufrn.dct.bsi.touchfy.domain.permission.Permission;
import com.ufrn.dct.bsi.touchfy.domain.permission.repository.PermissionRepository;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.role.Role;
import com.ufrn.dct.bsi.touchfy.domain.role.repository.RoleRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class CriarRoleUseCaseTest {

  private RoleRepository roleRepository;
  private PermissionRepository permissionRepository;
  private CriarRoleUseCase useCase;

  @BeforeEach
  void setup() {
    roleRepository = mock(RoleRepository.class);
    permissionRepository = mock(PermissionRepository.class);
    useCase = new CriarRoleUseCase(roleRepository, permissionRepository);
  }

  private Permission permissionMock() {
    return new Permission(1L, "READ");
  }

  private CreateRoleRequest requestValido() {
    return new CreateRoleRequest(ERole.OUVINTE, List.of(1L));
  }

  @Test
  void deveCriarRoleComSucesso() {
    final CreateRoleRequest request = requestValido();
    final Permission permission = permissionMock();

    when(permissionRepository.buscarPorId(1L)).thenReturn(Optional.of(permission));

    useCase.execute(request);

    final ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);
    verify(roleRepository).salvar(captor.capture());

    final Role roleSalva = captor.getValue();
    assertEquals(ERole.OUVINTE, roleSalva.getName());
    assertEquals(1, roleSalva.getPermissions().size());
    assertTrue(roleSalva.getPermissions().contains(permission));
  }

  @Test
  void deveLancarExcecaoQuandoNameForNulo() {
    final CreateRoleRequest request = new CreateRoleRequest(null, List.of(1L));

    final IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));

    assertEquals("O nome do perfil é obrigatório.", exception.getMessage());
    verify(roleRepository, never()).salvar(any());
  }

  @Test
  void deveLancarExcecaoQuandoPermissaoNaoForEncontrada() {
    final CreateRoleRequest request = requestValido();

    when(permissionRepository.buscarPorId(1L)).thenReturn(Optional.empty());

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(request));

    assertEquals("Permissão não encontrada para o ID: 1", exception.getMessage());
    verify(roleRepository, never()).salvar(any());
  }
}
