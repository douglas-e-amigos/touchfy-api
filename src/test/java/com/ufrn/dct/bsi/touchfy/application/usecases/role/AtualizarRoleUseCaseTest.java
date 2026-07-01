package com.ufrn.dct.bsi.touchfy.application.usecases.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.application.dtos.role.UpdateRoleRequest;
import com.ufrn.dct.bsi.touchfy.domain.permission.Permission;
import com.ufrn.dct.bsi.touchfy.domain.permission.repository.PermissionRepository;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.role.Role;
import com.ufrn.dct.bsi.touchfy.domain.role.repository.RoleRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class AtualizarRoleUseCaseTest {

  private RoleRepository roleRepository;
  private PermissionRepository permissionRepository;
  private AtualizarRoleUseCase useCase;

  @BeforeEach
  void setup() {
    roleRepository = mock(RoleRepository.class);
    permissionRepository = mock(PermissionRepository.class);
    useCase = new AtualizarRoleUseCase(roleRepository, permissionRepository);
  }

  private Role roleMock() {
    return new Role(1L, ERole.OUVINTE, Set.of());
  }

  private Permission permissionMock() {
    return new Permission(2L, "WRITE");
  }

  private UpdateRoleRequest requestValido() {
    return new UpdateRoleRequest(ERole.ARTISTA, List.of(2L));
  }

  @Test
  void deveAtualizarRoleComSucesso() {
    final Role role = roleMock();
    final Permission permission = permissionMock();
    final UpdateRoleRequest request = requestValido();

    when(roleRepository.buscarPorId(1L)).thenReturn(Optional.of(role));
    when(permissionRepository.buscarPorId(2L)).thenReturn(Optional.of(permission));

    useCase.execute(1L, request);

    final ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);
    verify(roleRepository).atualizar(captor.capture());

    final Role roleAtualizada = captor.getValue();
    assertEquals(1L, roleAtualizada.getId());
    assertEquals(ERole.ARTISTA, roleAtualizada.getName());
    assertEquals(1, roleAtualizada.getPermissions().size());
    assertTrue(roleAtualizada.getPermissions().contains(permission));
  }

  @Test
  void deveLancarExcecaoQuandoNameForNulo() {
    final UpdateRoleRequest request = new UpdateRoleRequest(null, List.of(2L));

    final IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(1L, request));

    assertEquals("O nome do perfil é obrigatório.", exception.getMessage());
    verifyNoInteractions(roleRepository);
  }

  @Test
  void deveLancarExcecaoQuandoRoleNaoExistir() {
    final UpdateRoleRequest request = requestValido();

    when(roleRepository.buscarPorId(1L)).thenReturn(Optional.empty());

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(1L, request));

    assertEquals("Perfil não encontrado para o ID: 1", exception.getMessage());
    verify(roleRepository, never()).atualizar(any());
  }

  @Test
  void deveLancarExcecaoQuandoPermissaoNaoForEncontrada() {
    final Role role = roleMock();
    final UpdateRoleRequest request = requestValido();

    when(roleRepository.buscarPorId(1L)).thenReturn(Optional.of(role));
    when(permissionRepository.buscarPorId(2L)).thenReturn(Optional.empty());

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(1L, request));

    assertEquals("Permissão não encontrada para o ID: 2", exception.getMessage());
    verify(roleRepository, never()).atualizar(any());
  }
}
