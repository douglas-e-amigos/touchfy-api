package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.MusicaResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.StreamingMusicaResponse;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.*;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class MusicaControllerTest {

  private MockMvc mockMvc;
  private final CriarMusicaUseCase criarMusicaUseCase = mock(CriarMusicaUseCase.class);
  private final AtualizarMusicaUseCase atualizarMusicaUseCase = mock(AtualizarMusicaUseCase.class);
  private final BuscarMusicaUseCase buscarMusicaUseCase = mock(BuscarMusicaUseCase.class);
  private final ConsultarMusicasDoArtistaUseCase consultarMusicasDoArtistaUseCase =
      mock(ConsultarMusicasDoArtistaUseCase.class);
  private final ConsultarMusicasUseCase consultarMusicasUseCase =
      mock(ConsultarMusicasUseCase.class);
  private final DeletarMusicaUseCase deletarMusicaUseCase = mock(DeletarMusicaUseCase.class);
  private final StreamingMusicaUseCase streamingMusicaUseCase = mock(StreamingMusicaUseCase.class);

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(
                new MusicaController(
                    criarMusicaUseCase,
                    atualizarMusicaUseCase,
                    buscarMusicaUseCase,
                    consultarMusicasDoArtistaUseCase,
                    consultarMusicasUseCase,
                    deletarMusicaUseCase,
                    streamingMusicaUseCase))
            .build();
  }

  @Test
  void deveCriarMusica() throws Exception {
    final var file =
        new MockMultipartFile("arquivo", "musica.mp3", "audio/mpeg", "conteudo".getBytes());
    mockMvc
        .perform(multipart("/musicas").file(file).param("nome", "Minha Musica"))
        .andExpect(status().isOk());
    verify(criarMusicaUseCase).execute(any());
  }

  @Test
  void deveAtualizarMusica() throws Exception {
    final UUID id = UUID.randomUUID();
    final var file =
        new MockMultipartFile("arquivo", "musica.mp3", "audio/mpeg", "conteudo".getBytes());
    mockMvc
        .perform(
            multipart("/musicas/{id}", id)
                .file(file)
                .with(
                    request -> {
                      request.setMethod("PATCH");
                      return request;
                    })
                .param("nome", "Nova Musica"))
        .andExpect(status().isOk());
    verify(atualizarMusicaUseCase).execute(eq(id), any());
  }

  @Test
  void deveConsultarMusicas() throws Exception {
    when(consultarMusicasUseCase.execute(null)).thenReturn(List.of());
    mockMvc.perform(get("/musicas")).andExpect(status().isOk());
  }

  @Test
  void deveConsultarMusicasDoArtista() throws Exception {
    final UUID id = UUID.randomUUID();
    when(consultarMusicasDoArtistaUseCase.execute(id)).thenReturn(List.of());
    mockMvc.perform(get("/musicas/artista/{artistaId}", id)).andExpect(status().isOk());
  }

  @Test
  void deveBuscarMusica() throws Exception {
    final UUID id = UUID.randomUUID();
    when(buscarMusicaUseCase.execute(id)).thenReturn(mock(MusicaResponse.class));
    mockMvc.perform(get("/musicas/{id}", id)).andExpect(status().isOk());
  }

  @Test
  void deveStreamMusica() throws Exception {
    final UUID id = UUID.randomUUID();
    when(streamingMusicaUseCase.execute(id, null))
        .thenReturn(
            new StreamingMusicaResponse(
                "musica.mp3", "audio/mpeg", new byte[] {1, 2, 3}, 0, 2, 3, false));
    mockMvc.perform(get("/musicas/stream/{id}", id)).andExpect(status().isOk());
  }

  @Test
  void deveDeletarMusica() throws Exception {
    final UUID id = UUID.randomUUID();
    mockMvc.perform(delete("/musicas/{id}", id)).andExpect(status().isOk());
    verify(deletarMusicaUseCase).execute(id);
  }
}
