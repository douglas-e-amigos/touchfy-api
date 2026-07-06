package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarGeneroMusicalRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarGeneroMusicalRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero.AtualizarGeneroMusicalUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero.ConsultarGenerosMusicaisUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero.CriarGeneroMusicalUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero.DeletarGeneroMusicalUseCase;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class GeneroMusicalControllerTest {

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final CriarGeneroMusicalUseCase criarGeneroMusicalUseCase =
      mock(CriarGeneroMusicalUseCase.class);
  private final AtualizarGeneroMusicalUseCase atualizarGeneroMusicalUseCase =
      mock(AtualizarGeneroMusicalUseCase.class);
  private final ConsultarGenerosMusicaisUseCase consultarGenerosMusicaisUseCase =
      mock(ConsultarGenerosMusicaisUseCase.class);
  private final DeletarGeneroMusicalUseCase deletarGeneroMusicalUseCase =
      mock(DeletarGeneroMusicalUseCase.class);

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(
                new GeneroMusicalController(
                    criarGeneroMusicalUseCase, atualizarGeneroMusicalUseCase,
                    consultarGenerosMusicaisUseCase, deletarGeneroMusicalUseCase))
            .build();
  }

  @Test
  void deveCriarGeneroMusical() throws Exception {
    final var request = new CriarGeneroMusicalRequest("Rock");
    mockMvc
        .perform(
            post("/musicas/generos-musicais")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
    verify(criarGeneroMusicalUseCase).execute(any());
  }

  @Test
  void deveAtualizarGeneroMusical() throws Exception {
    final UUID id = UUID.randomUUID();
    final var request = new AtualizarGeneroMusicalRequest("Pop");
    mockMvc
        .perform(
            patch("/musicas/generos-musicais/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
    verify(atualizarGeneroMusicalUseCase).execute(eq(id), any());
  }

  @Test
  void deveConsultarGenerosMusicais() throws Exception {
    when(consultarGenerosMusicaisUseCase.execute()).thenReturn(List.of());
    mockMvc.perform(get("/musicas/generos-musicais")).andExpect(status().isOk());
  }

  @Test
  void deveDeletarGeneroMusical() throws Exception {
    final UUID id = UUID.randomUUID();
    mockMvc.perform(delete("/musicas/generos-musicais/{id}", id)).andExpect(status().isOk());
    verify(deletarGeneroMusicalUseCase).execute(id);
  }
}
