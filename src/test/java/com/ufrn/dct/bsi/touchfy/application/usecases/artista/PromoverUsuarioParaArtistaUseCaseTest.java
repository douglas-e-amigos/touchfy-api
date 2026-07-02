package com.ufrn.dct.bsi.touchfy.application.usecases.artista;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.RoleEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PromoverUsuarioParaArtistaUseCaseTest {

  @Test
  void devePromoverUsuarioComSucesso() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final PromoverUsuarioParaArtistaUseCase useCase =
        new PromoverUsuarioParaArtistaUseCase(usuarioRepository);
    final UUID usuarioId = UUID.randomUUID();
    final var usuario =
        UsuarioEntity.builder()
            .id(usuarioId)
            .roles(Set.of(new RoleEntity(2L, ERole.OUVINTE, Set.of())))
            .build();

    when(usuarioRepository.acharPeloId(usuarioId)).thenReturn(Optional.of(usuario));

    useCase.execute(usuarioId);

    verify(usuarioRepository).adicionarRole(usuarioId, ERole.ARTISTA);
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioJaForArtista() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final PromoverUsuarioParaArtistaUseCase useCase =
        new PromoverUsuarioParaArtistaUseCase(usuarioRepository);
    final UUID usuarioId = UUID.randomUUID();
    final var usuario =
        UsuarioEntity.builder()
            .id(usuarioId)
            .roles(Set.of(new RoleEntity(1L, ERole.ARTISTA, Set.of())))
            .build();

    when(usuarioRepository.acharPeloId(usuarioId)).thenReturn(Optional.of(usuario));

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(usuarioId));

    assertTrue(exception.getMessage().contains("Conflito"));
    verify(usuarioRepository, never()).adicionarRole(any(), any());
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoExistir() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final PromoverUsuarioParaArtistaUseCase useCase =
        new PromoverUsuarioParaArtistaUseCase(usuarioRepository);
    final UUID usuarioId = UUID.randomUUID();

    when(usuarioRepository.acharPeloId(usuarioId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> useCase.execute(usuarioId));
    verify(usuarioRepository, never()).adicionarRole(any(), any());
  }
}
