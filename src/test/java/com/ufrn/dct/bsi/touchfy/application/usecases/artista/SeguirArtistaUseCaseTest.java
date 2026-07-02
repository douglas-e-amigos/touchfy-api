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

class SeguirArtistaUseCaseTest {

  @Test
  void deveSeguirArtistaComSucesso() {
    final ArtistaRepository artistaRepository = mock(ArtistaRepository.class);
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final SeguirArtistaUseCase useCase =
        new SeguirArtistaUseCase(artistaRepository, usuarioRepository);
    final UUID artistaId = UUID.randomUUID();
    final UUID usuarioId = UUID.randomUUID();
    final var artista =
        UsuarioEntity.builder()
            .id(artistaId)
            .roles(Set.of(new RoleEntity(1L, ERole.ARTISTA, Set.of())))
            .build();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.of(artista));
    when(artistaRepository.existeSeguindo(artistaId, usuarioId)).thenReturn(false);

    useCase.execute(artistaId, usuarioId);

    verify(artistaRepository).seguir(artistaId, usuarioId);
  }

  @Test
  void deveLancarExcecaoQuandoArtistaNaoExistir() {
    final ArtistaRepository artistaRepository = mock(ArtistaRepository.class);
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final SeguirArtistaUseCase useCase =
        new SeguirArtistaUseCase(artistaRepository, usuarioRepository);
    final UUID artistaId = UUID.randomUUID();
    final UUID usuarioId = UUID.randomUUID();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.empty());

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(artistaId, usuarioId));

    assertEquals("Artista não encontrado.", exception.getMessage());
    verify(artistaRepository, never()).seguir(any(), any());
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoForArtista() {
    final ArtistaRepository artistaRepository = mock(ArtistaRepository.class);
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final SeguirArtistaUseCase useCase =
        new SeguirArtistaUseCase(artistaRepository, usuarioRepository);
    final UUID artistaId = UUID.randomUUID();
    final UUID usuarioId = UUID.randomUUID();
    final var usuario =
        UsuarioEntity.builder()
            .id(artistaId)
            .roles(Set.of(new RoleEntity(2L, ERole.OUVINTE, Set.of())))
            .build();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.of(usuario));

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(artistaId, usuarioId));

    assertEquals("Artista não encontrado.", exception.getMessage());
    verify(artistaRepository, never()).seguir(any(), any());
  }

  @Test
  void deveLancarExcecaoQuandoJaEstiverSeguindo() {
    final ArtistaRepository artistaRepository = mock(ArtistaRepository.class);
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final SeguirArtistaUseCase useCase =
        new SeguirArtistaUseCase(artistaRepository, usuarioRepository);
    final UUID artistaId = UUID.randomUUID();
    final UUID usuarioId = UUID.randomUUID();
    final var artista =
        UsuarioEntity.builder()
            .id(artistaId)
            .roles(Set.of(new RoleEntity(1L, ERole.ARTISTA, Set.of())))
            .build();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.of(artista));
    when(artistaRepository.existeSeguindo(artistaId, usuarioId)).thenReturn(true);

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(artistaId, usuarioId));

    assertTrue(exception.getMessage().contains("Conflito"));
    verify(artistaRepository, never()).seguir(any(), any());
  }

  @Test
  void deveLancarExcecaoQuandoTentarSeguirASiMesmo() {
    final ArtistaRepository artistaRepository = mock(ArtistaRepository.class);
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final SeguirArtistaUseCase useCase =
        new SeguirArtistaUseCase(artistaRepository, usuarioRepository);
    final UUID mesmoId = UUID.randomUUID();

    assertThrows(IllegalArgumentException.class, () -> useCase.execute(mesmoId, mesmoId));

    verify(artistaRepository, never()).seguir(any(), any());
  }
}
