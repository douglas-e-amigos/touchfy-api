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

class BuscarPerfilArtistaUseCaseTest {

  @Test
  void deveRetornarPerfilCompleto() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final ArtistaRepository artistaRepository = mock(ArtistaRepository.class);
    final BuscarPerfilArtistaUseCase useCase =
        new BuscarPerfilArtistaUseCase(usuarioRepository, artistaRepository);
    final UUID artistaId = UUID.randomUUID();
    final var usuario =
        UsuarioEntity.builder()
            .id(artistaId)
            .nome("Artista Teste")
            .nomeUsuario("artista_teste")
            .descricao("Uma descrição")
            .caminhoDaImagemDePerfil("foto.jpg")
            .roles(Set.of(new RoleEntity(1L, ERole.ARTISTA, Set.of())))
            .build();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.of(usuario));
    when(artistaRepository.contarSeguidores(artistaId)).thenReturn(42L);

    final var response = useCase.execute(artistaId);

    assertEquals(artistaId, response.id());
    assertEquals("Artista Teste", response.nome());
    assertEquals("artista_teste", response.nomeUsuario());
    assertEquals("Uma descrição", response.descricao());
    assertEquals("foto.jpg", response.imagem());
    assertEquals(42L, response.numeroDeOuvintes());
    assertNull(response.posicaoRanking());
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoForArtista() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final ArtistaRepository artistaRepository = mock(ArtistaRepository.class);
    final BuscarPerfilArtistaUseCase useCase =
        new BuscarPerfilArtistaUseCase(usuarioRepository, artistaRepository);
    final UUID artistaId = UUID.randomUUID();
    final var usuario =
        UsuarioEntity.builder()
            .id(artistaId)
            .roles(Set.of(new RoleEntity(2L, ERole.OUVINTE, Set.of())))
            .build();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.of(usuario));

    assertThrows(RuntimeException.class, () -> useCase.execute(artistaId));
  }

  @Test
  void deveLancarExcecaoQuandoArtistaNaoExistir() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final ArtistaRepository artistaRepository = mock(ArtistaRepository.class);
    final BuscarPerfilArtistaUseCase useCase =
        new BuscarPerfilArtistaUseCase(usuarioRepository, artistaRepository);
    final UUID artistaId = UUID.randomUUID();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> useCase.execute(artistaId));
  }
}
