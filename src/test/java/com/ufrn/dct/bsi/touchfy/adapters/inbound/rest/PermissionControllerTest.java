package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.permission.CreatePermissionRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.permission.PermissionResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.permission.UpdatePermissionRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.permission.*;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class PermissionControllerTest {

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final CriarPermissionUseCase criarPermissionUseCase = mock(CriarPermissionUseCase.class);
  private final BuscarPermissionPorIdUseCase buscarPermissionPorIdUseCase =
      mock(BuscarPermissionPorIdUseCase.class);
  private final ListarPermissionsUseCase listarPermissionsUseCase =
      mock(ListarPermissionsUseCase.class);
  private final AtualizarPermissionUseCase atualizarPermissionUseCase =
      mock(AtualizarPermissionUseCase.class);
  private final ExcluirPermissionUseCase excluirPermissionUseCase =
      mock(ExcluirPermissionUseCase.class);

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(
                new PermissionController(
                    criarPermissionUseCase,
                    buscarPermissionPorIdUseCase,
                    listarPermissionsUseCase,
                    atualizarPermissionUseCase,
                    excluirPermissionUseCase))
            .build();
  }

  @Test
  void deveCriarPermission() throws Exception {
    final var request = new CreatePermissionRequest("permissao");
    mockMvc
        .perform(
            post("/permissions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
    verify(criarPermissionUseCase).execute(any());
  }

  @Test
  void deveListarPermissions() throws Exception {
    when(listarPermissionsUseCase.execute()).thenReturn(List.of());
    mockMvc.perform(get("/permissions")).andExpect(status().isOk());
  }

  @Test
  void deveBuscarPermissionPorId() throws Exception {
    when(buscarPermissionPorIdUseCase.execute(1L)).thenReturn(mock(PermissionResponse.class));
    mockMvc.perform(get("/permissions/{id}", 1L)).andExpect(status().isOk());
  }

  @Test
  void deveAtualizarPermission() throws Exception {
    final var request = new UpdatePermissionRequest("novaPermissao");
    mockMvc
        .perform(
            put("/permissions/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
    verify(atualizarPermissionUseCase).execute(eq(1L), any());
  }

  @Test
  void deveExcluirPermission() throws Exception {
    mockMvc.perform(delete("/permissions/{id}", 1L)).andExpect(status().isOk());
    verify(excluirPermissionUseCase).execute(1L);
  }
}
