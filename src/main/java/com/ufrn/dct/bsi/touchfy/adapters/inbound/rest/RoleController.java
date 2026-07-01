package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import com.ufrn.dct.bsi.touchfy.application.dtos.role.CreateRoleRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.role.RoleResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.role.UpdateRoleRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.role.*;
import com.ufrn.dct.bsi.touchfy.shared.dtos.NovoRecursoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoAtualizadoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoDeletadoResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

  private final CriarRoleUseCase criarRoleUseCase;
  private final BuscarRolePorIdUseCase buscarRolePorIdUseCase;
  private final ListarRolesUseCase listarRolesUseCase;
  private final AtualizarRoleUseCase atualizarRoleUseCase;
  private final ExcluirRoleUseCase excluirRoleUseCase;

  @PostMapping
  @PreAuthorize("hasAuthority('role:create')")
  public ResponseEntity<NovoRecursoResponse> criar(
      @RequestBody @Valid final CreateRoleRequest request) {
    criarRoleUseCase.execute(request);
    return ResponseEntity.ok(
        NovoRecursoResponse.builder()
            .criado(Boolean.TRUE)
            .criadoEm(LocalDate.now())
            .mensagem("Perfil criado com sucesso!")
            .build());
  }

  @GetMapping
  @PreAuthorize("hasAuthority('role:read')")
  public ResponseEntity<List<RoleResponse>> listar() {
    final List<RoleResponse> roles = listarRolesUseCase.execute();
    return ResponseEntity.ok(roles);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('role:read')")
  public ResponseEntity<RoleResponse> buscarPorId(@PathVariable final Long id) {
    final RoleResponse role = buscarRolePorIdUseCase.execute(id);
    return ResponseEntity.ok(role);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('role:patch')")
  public ResponseEntity<RecursoAtualizadoResponse> atualizar(
      @PathVariable final Long id, @RequestBody @Valid final UpdateRoleRequest request) {
    atualizarRoleUseCase.execute(id, request);
    return ResponseEntity.ok(
        RecursoAtualizadoResponse.builder()
            .atualizado(Boolean.TRUE)
            .atualizadoEm(LocalDate.now())
            .mensagem("Perfil atualizado com sucesso!")
            .build());
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('role:delete')")
  public ResponseEntity<RecursoDeletadoResponse> excluir(@PathVariable final Long id) {
    excluirRoleUseCase.execute(id);
    return ResponseEntity.ok(
        new RecursoDeletadoResponse("Perfil excluído com sucesso!", Boolean.TRUE, LocalDate.now()));
  }
}
