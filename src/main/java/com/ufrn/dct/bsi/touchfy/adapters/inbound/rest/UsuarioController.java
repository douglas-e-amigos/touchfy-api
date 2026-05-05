package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import com.ufrn.dct.bsi.touchfy.adapters.inbound.rest.routes.UsuarioRoute;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.AtualizarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.AutenticarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.CriarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.usuario.AtualizarUsuarioUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.usuario.AutenticarUsuarioUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.usuario.CriarUsuarioUseCase;
import com.ufrn.dct.bsi.touchfy.shared.dtos.NovoRecursoResponse;

import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoAtualizadoResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(UsuarioRoute.ROOT)
@AllArgsConstructor
public class UsuarioController {
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final CriarUsuarioUseCase criarUsuarioUseCase;

    @PostMapping(UsuarioRoute.CADASTRO)
    public ResponseEntity<NovoRecursoResponse> criarUsuario(
            @RequestBody @Valid final CriarUsuarioRequest request
    ) {
        criarUsuarioUseCase.execute(request);

        return ResponseEntity.ok(NovoRecursoResponse.builder()
                .criado(Boolean.TRUE)
                .criadoEm(LocalDate.now())
                .mensagem("Usuario criado com sucesso!")
                .build()
        );
    }

    @PostMapping(UsuarioRoute.LOGIN)
    public ResponseEntity<?> autenticarUsuario(@RequestBody @Valid final AutenticarUsuarioRequest request) {
        final String token = autenticarUsuarioUseCase.execute(request.nomeUsuario(), request.senha());
        return ResponseEntity.ok(Map.of("token", token));
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping(UsuarioRoute.ATUALIZAR)
    public ResponseEntity<RecursoAtualizadoResponse> atualizarUsuario(@PathVariable("id") final UUID id,
            @RequestBody @Valid final AtualizarUsuarioRequest request) {
        atualizarUsuarioUseCase.execute(id, request);

        return ResponseEntity.ok(RecursoAtualizadoResponse.builder()
                .atualizado(Boolean.TRUE)
                .atualizadoEm(LocalDate.now())
                .mensagem("Usuário atualizado com sucesso!")
                .build()
        );
    }
}
