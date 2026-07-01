package com.ufrn.dct.bsi.touchfy.application.usecases.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.RoleMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.role.RoleResponse;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.role.Role;
import com.ufrn.dct.bsi.touchfy.domain.role.repository.RoleRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BuscarRolePorIdUseCaseTest {

  private RoleRepository roleRepository;
  private RoleMapper roleMapper;
  private BuscarRolePorIdUseCase useCase;

  @BeforeEach
  void setup() {
    roleRepository = mock(RoleRepository.class);
    roleMapper = mock(RoleMapper.class);
    useCase = new BuscarRolePorIdUseCase(roleRepository, roleMapper);
  }

  private Role roleMock() {
    return new Role(1L, ERole.OUVINTE, Set.of());
  }

  private RoleResponse responseMock() {
    return new RoleResponse(1L, ERole.OUVINTE, List.of());
  }

  @Test
  void deveRetornarRoleQuandoEncontrada() {
    final Role role = roleMock();
    final RoleResponse response = responseMock();

    when(roleRepository.buscarPorId(1L)).thenReturn(Optional.of(role));
    when(roleMapper.toResponse(role)).thenReturn(response);

    final RoleResponse result = useCase.execute(1L);

    assertEquals(response, result);
    verify(roleRepository).buscarPorId(1L);
    verify(roleMapper).toResponse(role);
  }

  @Test
  void deveLancarExcecaoQuandoRoleNaoForEncontrada() {
    when(roleRepository.buscarPorId(1L)).thenReturn(Optional.empty());

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(1L));

    assertEquals("Perfil não encontrado para o ID: 1", exception.getMessage());
    verify(roleRepository).buscarPorId(1L);
    verifyNoInteractions(roleMapper);
  }
}
