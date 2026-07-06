package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarTagRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarTagRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag.AtualizarTagUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag.ConsultarTagsUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag.CriarTagUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag.DeletarTagUseCase;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class TagControllerTest {

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final CriarTagUseCase criarTagUseCase = mock(CriarTagUseCase.class);
  private final AtualizarTagUseCase atualizarTagUseCase = mock(AtualizarTagUseCase.class);
  private final ConsultarTagsUseCase consultarTagsUseCase = mock(ConsultarTagsUseCase.class);
  private final DeletarTagUseCase deletarTagUseCase = mock(DeletarTagUseCase.class);

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(
                new TagController(
                    criarTagUseCase, atualizarTagUseCase, consultarTagsUseCase, deletarTagUseCase))
            .build();
  }

  @Test
  void deveCriarTag() throws Exception {
    final var request = new CriarTagRequest("tags123");
    mockMvc
        .perform(
            post("/musicas/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
    verify(criarTagUseCase).execute(any());
  }

  @Test
  void deveAtualizarTag() throws Exception {
    final UUID id = UUID.randomUUID();
    final var request = new AtualizarTagRequest("novaTag");
    mockMvc
        .perform(
            patch("/musicas/tags/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
    verify(atualizarTagUseCase).execute(eq(id), any());
  }

  @Test
  void deveConsultarTags() throws Exception {
    when(consultarTagsUseCase.execute()).thenReturn(List.of());
    mockMvc.perform(get("/musicas/tags")).andExpect(status().isOk());
  }

  @Test
  void deveDeletarTag() throws Exception {
    final UUID id = UUID.randomUUID();
    mockMvc.perform(delete("/musicas/tags/{id}", id)).andExpect(status().isOk());
    verify(deletarTagUseCase).execute(id);
  }
}
