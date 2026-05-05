package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import com.ufrn.dct.bsi.touchfy.adapters.inbound.rest.routes.UsuarioRoute;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.AutenticarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.CriarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.usuario.AutenticarUsuarioUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.usuario.CriarUsuarioUseCase;
import com.ufrn.dct.bsi.touchfy.shared.dtos.NovoRecursoResponse;

import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping(UsuarioRoute.ROOT)
@AllArgsConstructor
public class UsuarioController {
    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;

    @PostMapping(UsuarioRoute.CADASTRO)
    public ResponseEntity<NovoRecursoResponse> criarUsuario(
            @RequestBody @Valid final CriarUsuarioRequest request
    ) {
        criarUsuarioUseCase.execute(request);

        return ResponseEntity.ok(NovoRecursoResponse.builder()
                .criado(true)
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
}
