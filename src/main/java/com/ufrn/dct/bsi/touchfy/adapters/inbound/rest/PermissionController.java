package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import com.ufrn.dct.bsi.touchfy.application.dtos.permission.CreatePermissionRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.permission.PermissionResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.permission.UpdatePermissionRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.permission.*;
import com.ufrn.dct.bsi.touchfy.shared.dtos.NovoRecursoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoAtualizadoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoDeletadoResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/permissions")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PermissionController {

    private final CriarPermissionUseCase criarPermissionUseCase;
    private final BuscarPermissionPorIdUseCase buscarPermissionPorIdUseCase;
    private final ListarPermissionsUseCase listarPermissionsUseCase;
    private final AtualizarPermissionUseCase atualizarPermissionUseCase;
    private final ExcluirPermissionUseCase excluirPermissionUseCase;

    @PostMapping
    public ResponseEntity<NovoRecursoResponse> criar(@RequestBody @Valid final CreatePermissionRequest request) {
        criarPermissionUseCase.execute(request);
        return ResponseEntity.ok(NovoRecursoResponse.builder()
                .criado(Boolean.TRUE)
                .criadoEm(LocalDate.now())
                .mensagem("Permissão criada com sucesso!")
                .build()
        );
    }

    @GetMapping
    public ResponseEntity<List<PermissionResponse>> listar() {
        final List<PermissionResponse> permissions = listarPermissionsUseCase.execute();
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponse> buscarPorId(@PathVariable final Long id) {
        final PermissionResponse permission = buscarPermissionPorIdUseCase.execute(id);
        return ResponseEntity.ok(permission);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecursoAtualizadoResponse> atualizar(
            @PathVariable final Long id,
            @RequestBody @Valid final UpdatePermissionRequest request
    ) {
        atualizarPermissionUseCase.execute(id, request);
        return ResponseEntity.ok(RecursoAtualizadoResponse.builder()
                .atualizado(Boolean.TRUE)
                .atualizadoEm(LocalDate.now())
                .mensagem("Permissão atualizada com sucesso!")
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RecursoDeletadoResponse> excluir(@PathVariable final Long id) {
        excluirPermissionUseCase.execute(id);
        return ResponseEntity.ok(new RecursoDeletadoResponse(
                "Permissão excluída com sucesso!",
                Boolean.TRUE,
                LocalDate.now()
        ));
    }
}
