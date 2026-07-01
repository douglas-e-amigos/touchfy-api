package com.ufrn.dct.bsi.touchfy.application.usecases.permission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.domain.permission.repository.PermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ExcluirPermissionUseCaseTest {

  private PermissionRepository permissionRepository;
  private ExcluirPermissionUseCase useCase;

  @BeforeEach
  void setup() {
    permissionRepository = mock(PermissionRepository.class);
    useCase = new ExcluirPermissionUseCase(permissionRepository);
  }

  @Test
  void deveExcluirPermissionComSucesso() {
    when(permissionRepository.existePorId(1L)).thenReturn(true);

    useCase.execute(1L);

    verify(permissionRepository).existePorId(1L);
    verify(permissionRepository).excluir(1L);
  }

  @Test
  void deveLancarExcecaoQuandoPermissionNaoExistir() {
    when(permissionRepository.existePorId(1L)).thenReturn(false);

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(1L));

    assertEquals("Permissão não encontrada para o ID: 1", exception.getMessage());
    verify(permissionRepository).existePorId(1L);
    verify(permissionRepository, never()).excluir(anyLong());
  }
}
