package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.security.UsuarioDetalhesImpl;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AdicionarMusicaAlbumRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AtualizarAlbumRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.CriarAlbumRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.album.*;
import com.ufrn.dct.bsi.touchfy.domain.album.models.TipoAlbum;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class AlbumControllerTest {

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final CriarAlbumUseCase criarAlbumUseCase = mock(CriarAlbumUseCase.class);
  private final AtualizarAlbumUseCase atualizarAlbumUseCase = mock(AtualizarAlbumUseCase.class);
  private final DeletarAlbumUseCase deletarAlbumUseCase = mock(DeletarAlbumUseCase.class);
  private final BuscarAlbumUseCase buscarAlbumUseCase = mock(BuscarAlbumUseCase.class);
  private final ListarAlbunsUseCase listarAlbunsUseCase = mock(ListarAlbunsUseCase.class);
  private final AdicionarMusicaAlbumUseCase adicionarMusicaAlbumUseCase =
      mock(AdicionarMusicaAlbumUseCase.class);
  private final SalvarAlbumUseCase salvarAlbumUseCase = mock(SalvarAlbumUseCase.class);
  private final RemoverAlbumSalvoUseCase removerAlbumSalvoUseCase =
      mock(RemoverAlbumSalvoUseCase.class);
  private final ListarAlbunsSalvosUseCase listarAlbunsSalvosUseCase =
      mock(ListarAlbunsSalvosUseCase.class);

  private final UUID usuarioId = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    final UsuarioDetalhesImpl principal =
        UsuarioDetalhesImpl.builder()
            .usuario(UsuarioEntity.builder().id(usuarioId).build())
            .build();
    final Authentication auth =
        new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
            principal, null, List.of());
    final SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(auth);
    SecurityContextHolder.setContext(context);

    mockMvc =
        MockMvcBuilders.standaloneSetup(
                new AlbumController(
                    criarAlbumUseCase,
                    atualizarAlbumUseCase,
                    deletarAlbumUseCase,
                    buscarAlbumUseCase,
                    listarAlbunsUseCase,
                    adicionarMusicaAlbumUseCase,
                    salvarAlbumUseCase,
                    removerAlbumSalvoUseCase,
                    listarAlbunsSalvosUseCase))
            .build();
  }

  @Test
  void deveCriarAlbum() throws Exception {
    final var request = new CriarAlbumRequest("Album", "Desc", null, null, TipoAlbum.ALBUM);

    mockMvc
        .perform(
            post("/albuns")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.criado").value(true));

    verify(criarAlbumUseCase).execute(any(CriarAlbumRequest.class), eq(usuarioId));
  }

  @Test
  void deveAtualizarAlbum() throws Exception {
    final UUID id = UUID.randomUUID();
    final var request =
        new AtualizarAlbumRequest("Novo Nome", "Nova Desc", null, null, TipoAlbum.ALBUM);

    mockMvc
        .perform(
            put("/albuns/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.atualizado").value(true));
  }

  @Test
  void deveDeletarAlbum() throws Exception {
    final UUID id = UUID.randomUUID();

    mockMvc.perform(delete("/albuns/{id}", id)).andExpect(status().isOk());

    verify(deletarAlbumUseCase).execute(eq(id), eq(usuarioId));
  }

  @Test
  void deveBuscarAlbum() throws Exception {
    final UUID id = UUID.randomUUID();
    when(buscarAlbumUseCase.execute(id)).thenReturn(mock(AlbumResponse.class));

    mockMvc.perform(get("/albuns/{id}", id)).andExpect(status().isOk());
  }

  @Test
  void deveListarAlbuns() throws Exception {
    when(listarAlbunsUseCase.execute()).thenReturn(List.of());

    mockMvc.perform(get("/albuns")).andExpect(status().isOk());
  }

  @Test
  void deveAdicionarMusica() throws Exception {
    final UUID albumId = UUID.randomUUID();
    final var request = new AdicionarMusicaAlbumRequest(UUID.randomUUID());

    mockMvc
        .perform(
            post("/albuns/{id}/musicas", albumId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  void deveSalvarAlbum() throws Exception {
    final UUID id = UUID.randomUUID();

    mockMvc.perform(post("/albuns/{id}/salvar", id)).andExpect(status().isOk());

    verify(salvarAlbumUseCase).execute(eq(id), eq(usuarioId));
  }

  @Test
  void deveRemoverAlbumSalvo() throws Exception {
    final UUID id = UUID.randomUUID();

    mockMvc.perform(delete("/albuns/{id}/salvar", id)).andExpect(status().isOk());

    verify(removerAlbumSalvoUseCase).execute(eq(id), eq(usuarioId));
  }
}
