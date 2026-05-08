package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import com.ufrn.dct.bsi.touchfy.adapters.inbound.rest.routes.UsuarioRoute;

import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.*;

import com.ufrn.dct.bsi.touchfy.application.usecases.usuario.*;

import com.ufrn.dct.bsi.touchfy.shared.dtos.NovoRecursoResponse;

import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoAtualizadoResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import lombok.AllArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping(UsuarioRoute.ROOT)
@AllArgsConstructor
public class UsuarioController {
    private final AtualizarFotoPerfilUsuarioUseCase atualizarFotoPerfilUsuarioUseCase;
    private final AtualizarUsuarioUseCase atualizarUsuarioUseCase;
    private final AutenticarUsuarioUseCase autenticarUsuarioUseCase;
    private final CriarUsuarioUseCase criarUsuarioUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;

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
    public ResponseEntity<TokenResponse> autenticarUsuario(
            @RequestBody @Valid final AutenticarUsuarioRequest request) {
        final var response = autenticarUsuarioUseCase.execute(request.nomeUsuario(), request.senha());
        return ResponseEntity.ok(response);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping(UsuarioRoute.ATUALIZAR)
    public ResponseEntity<RecursoAtualizadoResponse> atualizarUsuario(
            @PathVariable("id") final UUID id,
            @RequestBody @Valid final AtualizarUsuarioRequest request) {
        atualizarUsuarioUseCase.execute(id, request);

        return ResponseEntity.ok(RecursoAtualizadoResponse.builder()
                .atualizado(Boolean.TRUE)
                .atualizadoEm(LocalDate.now())
                .mensagem("Usuário atualizado com sucesso!")
                .build()
        );
    }

    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping(value = UsuarioRoute.ATUALIZAR_FOTO_DE_PERFIL, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecursoAtualizadoResponse> atualizarFotoDePerfil(
            @PathVariable("id") final UUID id,
            @ModelAttribute @Valid final AtualizarFotoPerfilUsuarioRequest request) {
        atualizarFotoPerfilUsuarioUseCase.execute(id, request);

        return ResponseEntity.ok(RecursoAtualizadoResponse.builder()
                .atualizado(Boolean.TRUE)
                .atualizadoEm(LocalDate.now())
                .mensagem("Foto de perfil atualizada com sucesso!")
                .build()
        );
    }

    @PostMapping(UsuarioRoute.REFRESH_TOKEN)
    public ResponseEntity<TokenResponse> refresh(@RequestBody @Valid final RefreshTokenRequest request) {
        final var response = refreshTokenUseCase.execute(request.refreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping(UsuarioRoute.LOGOUT)
    public ResponseEntity<Void> logout(@RequestBody @Valid final RefreshTokenRequest request) {
        logoutUseCase.execute(request.refreshToken());
        return ResponseEntity.noContent().build();
    }
}
