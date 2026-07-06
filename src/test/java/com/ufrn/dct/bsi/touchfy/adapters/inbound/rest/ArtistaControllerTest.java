package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.security.UsuarioDetalhesImpl;
import com.ufrn.dct.bsi.touchfy.application.dtos.artista.*;
import com.ufrn.dct.bsi.touchfy.application.usecases.artista.*;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ArtistaControllerTest {

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final SeguirArtistaUseCase seguirArtistaUseCase = mock(SeguirArtistaUseCase.class);
  private final BloquearArtistaUseCase bloquearArtistaUseCase = mock(BloquearArtistaUseCase.class);
  private final ConsultarDiscografiaArtistaUseCase consultarDiscografiaArtistaUseCase =
      mock(ConsultarDiscografiaArtistaUseCase.class);
  private final BuscarPerfilArtistaUseCase buscarPerfilArtistaUseCase =
      mock(BuscarPerfilArtistaUseCase.class);
  private final GerarCompartilhamentoArtistaUseCase gerarCompartilhamentoArtistaUseCase =
      mock(GerarCompartilhamentoArtistaUseCase.class);
  private final PromoverUsuarioParaArtistaUseCase promoverUsuarioParaArtistaUseCase =
      mock(PromoverUsuarioParaArtistaUseCase.class);
  private final AtualizarDadosArtistaUseCase atualizarDadosArtistaUseCase =
      mock(AtualizarDadosArtistaUseCase.class);
  private final DesativarStatusArtistaUseCase desativarStatusArtistaUseCase =
      mock(DesativarStatusArtistaUseCase.class);

  private final UUID usuarioId = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    final var principal =
        UsuarioDetalhesImpl.builder()
            .usuario(UsuarioEntity.builder().id(usuarioId).build())
            .build();
    final var auth =
        new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
            principal, null, List.of());
    final var context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(auth);
    SecurityContextHolder.setContext(context);

    mockMvc =
        MockMvcBuilders.standaloneSetup(
                new ArtistaController(
                    seguirArtistaUseCase,
                    bloquearArtistaUseCase,
                    consultarDiscografiaArtistaUseCase,
                    buscarPerfilArtistaUseCase,
                    gerarCompartilhamentoArtistaUseCase,
                    promoverUsuarioParaArtistaUseCase,
                    atualizarDadosArtistaUseCase,
                    desativarStatusArtistaUseCase))
            .build();
  }

  @Test
  void deveSeguirArtista() throws Exception {
    final UUID id = UUID.randomUUID();
    mockMvc.perform(post("/artistas/{id}/seguir", id)).andExpect(status().isOk());
    verify(seguirArtistaUseCase).execute(eq(id), eq(usuarioId));
  }

  @Test
  void deveBloquearArtista() throws Exception {
    final UUID id = UUID.randomUUID();
    mockMvc.perform(post("/artistas/{id}/bloquear", id)).andExpect(status().isOk());
    verify(bloquearArtistaUseCase).execute(eq(id), eq(usuarioId));
  }

  @Test
  void deveConsultarDiscografia() throws Exception {
    final UUID id = UUID.randomUUID();
    when(consultarDiscografiaArtistaUseCase.execute(id))
        .thenReturn(mock(ArtistaDiscografiaResponse.class));
    mockMvc.perform(get("/artistas/{id}/discografia", id)).andExpect(status().isOk());
  }

  @Test
  void deveBuscarPerfil() throws Exception {
    final UUID id = UUID.randomUUID();
    when(buscarPerfilArtistaUseCase.execute(id)).thenReturn(mock(ArtistaPerfilResponse.class));
    mockMvc.perform(get("/artistas/{id}", id)).andExpect(status().isOk());
  }

  @Test
  void deveGerarCompartilhamento() throws Exception {
    final UUID id = UUID.randomUUID();
    when(gerarCompartilhamentoArtistaUseCase.execute(id))
        .thenReturn(mock(ArtistaCompartilhamentoResponse.class));
    mockMvc.perform(get("/artistas/{id}/compartilhamento", id)).andExpect(status().isOk());
  }

  @Test
  void deveAtualizarDadosArtista() throws Exception {
    final UUID id = UUID.randomUUID();
    final var request = new AtualizarDadosArtistaRequest("Novo Nome", "Nova descricao");
    mockMvc
        .perform(
            patch("/artistas/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  void deveDesativarStatus() throws Exception {
    final UUID id = UUID.randomUUID();
    mockMvc.perform(delete("/artistas/{id}/status", id)).andExpect(status().isOk());
    verify(desativarStatusArtistaUseCase).execute(eq(id), eq(usuarioId));
  }
}
