package com.ufrn.dct.bsi.touchfy.application.usecases.artista;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.RoleEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.domain.artista.repositories.ArtistaRepository;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class BloquearArtistaUseCaseTest {

  @Test
  void deveBloquearArtistaComSucesso() {
    final ArtistaRepository artistaRepository = mock(ArtistaRepository.class);
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final BloquearArtistaUseCase useCase =
        new BloquearArtistaUseCase(artistaRepository, usuarioRepository);
    final UUID artistaId = UUID.randomUUID();
    final UUID usuarioId = UUID.randomUUID();
    final var artista =
        UsuarioEntity.builder()
            .id(artistaId)
            .roles(Set.of(new RoleEntity(1L, ERole.ARTISTA, Set.of())))
            .build();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.of(artista));
    when(artistaRepository.existeBloqueado(artistaId, usuarioId)).thenReturn(false);

    useCase.execute(artistaId, usuarioId);

    verify(artistaRepository).bloquear(artistaId, usuarioId);
  }

  @Test
  void deveLancarExcecaoQuandoArtistaNaoExistir() {
    final ArtistaRepository artistaRepository = mock(ArtistaRepository.class);
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final BloquearArtistaUseCase useCase =
        new BloquearArtistaUseCase(artistaRepository, usuarioRepository);
    final UUID artistaId = UUID.randomUUID();
    final UUID usuarioId = UUID.randomUUID();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> useCase.execute(artistaId, usuarioId));
    verify(artistaRepository, never()).bloquear(any(), any());
  }

  @Test
  void deveLancarExcecaoQuandoJaEstiverBloqueado() {
    final ArtistaRepository artistaRepository = mock(ArtistaRepository.class);
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final BloquearArtistaUseCase useCase =
        new BloquearArtistaUseCase(artistaRepository, usuarioRepository);
    final UUID artistaId = UUID.randomUUID();
    final UUID usuarioId = UUID.randomUUID();
    final var artista =
        UsuarioEntity.builder()
            .id(artistaId)
            .roles(Set.of(new RoleEntity(1L, ERole.ARTISTA, Set.of())))
            .build();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.of(artista));
    when(artistaRepository.existeBloqueado(artistaId, usuarioId)).thenReturn(true);

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(artistaId, usuarioId));

    assertTrue(exception.getMessage().contains("Conflito"));
    verify(artistaRepository, never()).bloquear(any(), any());
  }
}
