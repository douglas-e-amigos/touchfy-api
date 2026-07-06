package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.security.UsuarioDetalhesImpl;
import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.*;
import com.ufrn.dct.bsi.touchfy.application.usecases.playlist.*;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Visibilidade;
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

class PlaylistControllerTest {

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final CriarPlaylistUseCase criarPlaylistUseCase = mock(CriarPlaylistUseCase.class);
  private final AtualizarPlaylistUseCase atualizarPlaylistUseCase =
      mock(AtualizarPlaylistUseCase.class);
  private final DeletarPlaylistUseCase deletarPlaylistUseCase = mock(DeletarPlaylistUseCase.class);
  private final BuscarPlaylistUseCase buscarPlaylistUseCase = mock(BuscarPlaylistUseCase.class);
  private final ListarPlaylistsUsuarioUseCase listarPlaylistsUsuarioUseCase =
      mock(ListarPlaylistsUsuarioUseCase.class);
  private final AdicionarMusicaPlaylistUseCase adicionarMusicaPlaylistUseCase =
      mock(AdicionarMusicaPlaylistUseCase.class);
  private final RemoverMusicaPlaylistUseCase removerMusicaPlaylistUseCase =
      mock(RemoverMusicaPlaylistUseCase.class);

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
                new PlaylistController(
                    criarPlaylistUseCase,
                    atualizarPlaylistUseCase,
                    deletarPlaylistUseCase,
                    buscarPlaylistUseCase,
                    listarPlaylistsUsuarioUseCase,
                    adicionarMusicaPlaylistUseCase,
                    removerMusicaPlaylistUseCase))
            .build();
  }

  @Test
  void deveCriarPlaylist() throws Exception {
    final var request =
        new CriarPlaylistRequest("Playlist", "Desc", Visibilidade.PUBLICA, List.of());

    mockMvc
        .perform(
            post("/playlists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.criado").value(true));

    verify(criarPlaylistUseCase).execute(any(CriarPlaylistRequest.class), eq(usuarioId));
  }

  @Test
  void deveAtualizarPlaylist() throws Exception {
    final UUID id = UUID.randomUUID();
    final var request =
        new AtualizarPlaylistRequest("Novo Nome", "Nova Desc", Visibilidade.PRIVADA, null);

    mockMvc
        .perform(
            put("/playlists/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.atualizado").value(true));
  }

  @Test
  void deveDeletarPlaylist() throws Exception {
    final UUID id = UUID.randomUUID();

    mockMvc.perform(delete("/playlists/{id}", id)).andExpect(status().isOk());

    verify(deletarPlaylistUseCase).execute(eq(id), eq(usuarioId));
  }

  @Test
  void deveBuscarPlaylist() throws Exception {
    final UUID id = UUID.randomUUID();

    mockMvc.perform(get("/playlists/{id}", id)).andExpect(status().isOk());

    verify(buscarPlaylistUseCase).execute(eq(id), eq(usuarioId));
  }

  @Test
  void deveListarPlaylists() throws Exception {
    mockMvc.perform(get("/playlists")).andExpect(status().isOk());

    verify(listarPlaylistsUsuarioUseCase).execute(eq(usuarioId));
  }

  @Test
  void deveAdicionarMusica() throws Exception {
    final UUID id = UUID.randomUUID();
    final var request = new AdicionarMusicaPlaylistRequest(UUID.randomUUID());

    mockMvc
        .perform(
            post("/playlists/{id}/musicas", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  void deveRemoverMusica() throws Exception {
    final UUID id = UUID.randomUUID();
    final UUID musicaId = UUID.randomUUID();

    mockMvc
        .perform(delete("/playlists/{id}/musicas/{musicaId}", id, musicaId))
        .andExpect(status().isOk());

    verify(removerMusicaPlaylistUseCase).execute(eq(id), eq(musicaId), eq(usuarioId));
  }
}
