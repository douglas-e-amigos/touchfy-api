package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.CriarUsuarioRequest;
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

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuarioController {
    private final CriarUsuarioUseCase criarUsuarioUseCase;

    @PostMapping
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
}
