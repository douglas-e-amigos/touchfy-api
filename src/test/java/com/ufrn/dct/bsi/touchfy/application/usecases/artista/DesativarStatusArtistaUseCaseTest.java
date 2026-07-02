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

class DesativarStatusArtistaUseCaseTest {

  @Test
  void deveDesativarStatusComSucesso() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final DesativarStatusArtistaUseCase useCase =
        new DesativarStatusArtistaUseCase(usuarioRepository);
    final UUID artistaId = UUID.randomUUID();
    final var artista =
        UsuarioEntity.builder()
            .id(artistaId)
            .roles(Set.of(new RoleEntity(1L, ERole.ARTISTA, Set.of())))
            .build();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.of(artista));

    useCase.execute(artistaId, artistaId);

    verify(usuarioRepository).removerRole(artistaId, ERole.ARTISTA);
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioDiferenteDoDono() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final DesativarStatusArtistaUseCase useCase =
        new DesativarStatusArtistaUseCase(usuarioRepository);
    final UUID artistaId = UUID.randomUUID();
    final UUID outroUsuarioId = UUID.randomUUID();

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(artistaId, outroUsuarioId));

    assertTrue(exception.getMessage().contains("não autorizado"));
    verify(usuarioRepository, never()).removerRole(any(), any());
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoForArtista() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final DesativarStatusArtistaUseCase useCase =
        new DesativarStatusArtistaUseCase(usuarioRepository);
    final UUID artistaId = UUID.randomUUID();
    final var usuario =
        UsuarioEntity.builder()
            .id(artistaId)
            .roles(Set.of(new RoleEntity(2L, ERole.OUVINTE, Set.of())))
            .build();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.of(usuario));

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(artistaId, artistaId));

    assertTrue(exception.getMessage().contains("Conflito"));
    verify(usuarioRepository, never()).removerRole(any(), any());
  }
}
