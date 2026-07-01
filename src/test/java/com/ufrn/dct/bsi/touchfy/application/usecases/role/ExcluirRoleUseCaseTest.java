package com.ufrn.dct.bsi.touchfy.application.usecases.role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.domain.role.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExcluirRoleUseCaseTest {

  private RoleRepository roleRepository;
  private ExcluirRoleUseCase useCase;

  @BeforeEach
  void setup() {
    roleRepository = mock(RoleRepository.class);
    useCase = new ExcluirRoleUseCase(roleRepository);
  }

  @Test
  void deveExcluirRoleComSucesso() {
    when(roleRepository.existePorId(1L)).thenReturn(true);

    useCase.execute(1L);

    verify(roleRepository).existePorId(1L);
    verify(roleRepository).excluir(1L);
  }

  @Test
  void deveLancarExcecaoQuandoRoleNaoExistir() {
    when(roleRepository.existePorId(1L)).thenReturn(false);

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(1L));

    assertEquals("Perfil não encontrado para o ID: 1", exception.getMessage());
    verify(roleRepository).existePorId(1L);
    verify(roleRepository, never()).excluir(anyLong());
  }
}
