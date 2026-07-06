package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.security.UsuarioDetalhesImpl;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.*;
import com.ufrn.dct.bsi.touchfy.application.usecases.usuario.*;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class UsuarioControllerTest {

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
  private final AtualizarFotoPerfilUsuarioUseCase atualizarFotoPerfilUsuarioUseCase =
      mock(AtualizarFotoPerfilUsuarioUseCase.class);
  private final AtualizarUsuarioUseCase atualizarUsuarioUseCase =
      mock(AtualizarUsuarioUseCase.class);
  private final AutenticarUsuarioUseCase autenticarUsuarioUseCase =
      mock(AutenticarUsuarioUseCase.class);
  private final BuscarUsuarioUseCase buscarUsuarioUseCase = mock(BuscarUsuarioUseCase.class);
  private final CriarUsuarioUseCase criarUsuarioUseCase = mock(CriarUsuarioUseCase.class);
  private final BuscarUsuarioLogadoUseCase buscarUsuarioLogadoUseCase =
      mock(BuscarUsuarioLogadoUseCase.class);
  private final DesativarUsuarioUseCase desativarUsuarioUseCase =
      mock(DesativarUsuarioUseCase.class);
  private final RefreshTokenUseCase refreshTokenUseCase = mock(RefreshTokenUseCase.class);
  private final LogoutUseCase logoutUseCase = mock(LogoutUseCase.class);

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
                new UsuarioController(
                    atualizarFotoPerfilUsuarioUseCase,
                    atualizarUsuarioUseCase,
                    autenticarUsuarioUseCase,
                    buscarUsuarioUseCase,
                    criarUsuarioUseCase,
                    buscarUsuarioLogadoUseCase,
                    desativarUsuarioUseCase,
                    refreshTokenUseCase,
                    logoutUseCase))
            .build();
  }

  @Test
  void deveCriarUsuario() throws Exception {
    final var request =
        new CriarUsuarioRequest(
            "Nome",
            "usuario",
            "senha123",
            "senha123",
            "email@test.com",
            LocalDate.of(2000, 1, 1),
            ERole.OUVINTE);
    mockMvc
        .perform(
            post("/usuarios/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
    verify(criarUsuarioUseCase).execute(any());
  }

  @Test
  void deveAutenticarUsuario() throws Exception {
    final var request = new AutenticarUsuarioRequest("usuario", "senha");
    when(autenticarUsuarioUseCase.execute("usuario", "senha"))
        .thenReturn(mock(TokenResponse.class));
    mockMvc
        .perform(
            post("/usuarios/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  void deveAtualizarUsuario() throws Exception {
    final UUID id = UUID.randomUUID();
    final var request =
        new AtualizarUsuarioRequest("NovoNome", "novousuario", LocalDate.of(1999, 5, 10));
    mockMvc
        .perform(
            patch("/usuarios/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
    verify(atualizarUsuarioUseCase).execute(eq(id), any());
  }

  @Test
  void deveRefreshToken() throws Exception {
    final var request = new RefreshTokenRequest("token");
    when(refreshTokenUseCase.execute("token")).thenReturn(mock(TokenResponse.class));
    mockMvc
        .perform(
            post("/usuarios/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  void deveLogout() throws Exception {
    final var request = new RefreshTokenRequest("token");
    mockMvc
        .perform(
            post("/usuarios/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNoContent());
    verify(logoutUseCase).execute("token");
  }

  @Test
  void deveBuscarUsuarioLogado() throws Exception {
    when(buscarUsuarioLogadoUseCase.execute()).thenReturn(mock(UsuarioResponse.class));
    mockMvc.perform(get("/usuarios/me")).andExpect(status().isOk());
  }

  @Test
  void deveDesativarUsuario() throws Exception {
    final UUID id = UUID.randomUUID();
    mockMvc.perform(delete("/usuarios/{id}", id)).andExpect(status().isOk());
    verify(desativarUsuarioUseCase).execute(id);
  }

  @Test
  void deveBuscarUsuarioPorId() throws Exception {
    final UUID id = UUID.randomUUID();
    when(buscarUsuarioUseCase.execute(id)).thenReturn(mock(UsuarioResponse.class));
    mockMvc.perform(get("/usuarios/{id}", id)).andExpect(status().isOk());
  }
}
