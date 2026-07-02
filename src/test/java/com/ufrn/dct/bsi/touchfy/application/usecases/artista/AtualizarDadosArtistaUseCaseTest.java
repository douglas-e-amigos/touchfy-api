package com.ufrn.dct.bsi.touchfy.application.usecases.artista;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.RoleEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.application.dtos.artista.AtualizarDadosArtistaRequest;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AtualizarDadosArtistaUseCaseTest {

  @Test
  void deveAtualizarDadosComSucesso() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final AtualizarDadosArtistaUseCase useCase =
        new AtualizarDadosArtistaUseCase(usuarioRepository);
    final UUID artistaId = UUID.randomUUID();
    final var request = new AtualizarDadosArtistaRequest("Novo Nome", "Nova descrição");
    final var artista =
        UsuarioEntity.builder()
            .id(artistaId)
            .roles(Set.of(new RoleEntity(1L, ERole.ARTISTA, Set.of())))
            .build();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.of(artista));

    useCase.execute(artistaId, request, artistaId);

    verify(usuarioRepository).atualizarDadosArtista(artistaId, request);
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioDiferenteDoDono() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final AtualizarDadosArtistaUseCase useCase =
        new AtualizarDadosArtistaUseCase(usuarioRepository);
    final UUID artistaId = UUID.randomUUID();
    final UUID outroUsuarioId = UUID.randomUUID();
    final var request = new AtualizarDadosArtistaRequest("Nome", null);

    final RuntimeException exception =
        assertThrows(
            RuntimeException.class, () -> useCase.execute(artistaId, request, outroUsuarioId));

    assertTrue(exception.getMessage().contains("não autorizado"));
    verify(usuarioRepository, never()).atualizarDadosArtista(any(), any());
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoForArtista() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final AtualizarDadosArtistaUseCase useCase =
        new AtualizarDadosArtistaUseCase(usuarioRepository);
    final UUID artistaId = UUID.randomUUID();
    final var request = new AtualizarDadosArtistaRequest("Nome", null);
    final var usuario =
        UsuarioEntity.builder()
            .id(artistaId)
            .roles(Set.of(new RoleEntity(2L, ERole.OUVINTE, Set.of())))
            .build();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.of(usuario));

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(artistaId, request, artistaId));

    assertTrue(exception.getMessage().contains("não autorizado"));
    verify(usuarioRepository, never()).atualizarDadosArtista(any(), any());
  }

  @Test
  void deveLancarExcecaoQuandoRequestForVazio() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final AtualizarDadosArtistaUseCase useCase =
        new AtualizarDadosArtistaUseCase(usuarioRepository);
    final UUID artistaId = UUID.randomUUID();
    final var request = new AtualizarDadosArtistaRequest(null, null);
    final var artista =
        UsuarioEntity.builder()
            .id(artistaId)
            .roles(Set.of(new RoleEntity(1L, ERole.ARTISTA, Set.of())))
            .build();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.of(artista));

    assertThrows(
        IllegalArgumentException.class, () -> useCase.execute(artistaId, request, artistaId));

    verify(usuarioRepository, never()).atualizarDadosArtista(any(), any());
  }
}
