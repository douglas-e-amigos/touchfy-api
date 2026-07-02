package com.ufrn.dct.bsi.touchfy.application.usecases.artista;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class GerarCompartilhamentoArtistaUseCaseTest {

  @Test
  void deveGerarLinkNoFormatoEsperado() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final GerarCompartilhamentoArtistaUseCase useCase =
        new GerarCompartilhamentoArtistaUseCase(usuarioRepository, "http://localhost:3000");
    final UUID artistaId = UUID.randomUUID();
    final var usuario =
        UsuarioEntity.builder()
            .id(artistaId)
            .nome("Artista Teste")
            .caminhoDaImagemDePerfil("foto.jpg")
            .build();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.of(usuario));

    final var response = useCase.execute(artistaId);

    assertEquals("http://localhost:3000/artistas/" + artistaId, response.link());
    assertEquals("Artista Teste", response.nomeArtista());
    assertEquals("foto.jpg", response.imagem());
  }

  @Test
  void deveLancarExcecaoQuandoArtistaNaoExistir() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final GerarCompartilhamentoArtistaUseCase useCase =
        new GerarCompartilhamentoArtistaUseCase(usuarioRepository, "http://localhost:3000");
    final UUID artistaId = UUID.randomUUID();

    when(usuarioRepository.acharPeloId(artistaId)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> useCase.execute(artistaId));
  }
}
