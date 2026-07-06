package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.role.CreateRoleRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.role.RoleResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.role.UpdateRoleRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.role.*;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class RoleControllerTest {

  private MockMvc mockMvc;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final CriarRoleUseCase criarRoleUseCase = mock(CriarRoleUseCase.class);
  private final BuscarRolePorIdUseCase buscarRolePorIdUseCase = mock(BuscarRolePorIdUseCase.class);
  private final ListarRolesUseCase listarRolesUseCase = mock(ListarRolesUseCase.class);
  private final AtualizarRoleUseCase atualizarRoleUseCase = mock(AtualizarRoleUseCase.class);
  private final ExcluirRoleUseCase excluirRoleUseCase = mock(ExcluirRoleUseCase.class);

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(
                new RoleController(
                    criarRoleUseCase,
                    buscarRolePorIdUseCase,
                    listarRolesUseCase,
                    atualizarRoleUseCase,
                    excluirRoleUseCase))
            .build();
  }

  @Test
  void deveCriarRole() throws Exception {
    final var request = new CreateRoleRequest(ERole.ADMIN, List.of(1L, 2L));
    mockMvc
        .perform(
            post("/roles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
    verify(criarRoleUseCase).execute(any());
  }

  @Test
  void deveListarRoles() throws Exception {
    when(listarRolesUseCase.execute()).thenReturn(List.of());
    mockMvc.perform(get("/roles")).andExpect(status().isOk());
  }

  @Test
  void deveBuscarRolePorId() throws Exception {
    when(buscarRolePorIdUseCase.execute(1L)).thenReturn(mock(RoleResponse.class));
    mockMvc.perform(get("/roles/{id}", 1L)).andExpect(status().isOk());
  }

  @Test
  void deveAtualizarRole() throws Exception {
    final var request = new UpdateRoleRequest(ERole.OUVINTE, List.of(1L));
    mockMvc
        .perform(
            put("/roles/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
    verify(atualizarRoleUseCase).execute(eq(1L), any());
  }

  @Test
  void deveExcluirRole() throws Exception {
    mockMvc.perform(delete("/roles/{id}", 1L)).andExpect(status().isOk());
    verify(excluirRoleUseCase).execute(1L);
  }
}
