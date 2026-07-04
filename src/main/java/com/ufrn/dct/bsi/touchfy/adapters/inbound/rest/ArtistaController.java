package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.security.UsuarioDetalhesImpl;
import com.ufrn.dct.bsi.touchfy.application.dtos.artista.ArtistaCompartilhamentoResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.artista.ArtistaDiscografiaResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.artista.ArtistaPerfilResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.artista.AtualizarDadosArtistaRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.artista.AtualizarDadosArtistaUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.artista.BloquearArtistaUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.artista.BuscarPerfilArtistaUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.artista.ConsultarDiscografiaArtistaUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.artista.DesativarStatusArtistaUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.artista.GerarCompartilhamentoArtistaUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.artista.PromoverUsuarioParaArtistaUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.artista.SeguirArtistaUseCase;
import com.ufrn.dct.bsi.touchfy.shared.dtos.NovoRecursoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoAtualizadoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoDeletadoResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/artistas")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ArtistaController {
  private final SeguirArtistaUseCase seguirArtistaUseCase;
  private final BloquearArtistaUseCase bloquearArtistaUseCase;
  private final ConsultarDiscografiaArtistaUseCase consultarDiscografiaArtistaUseCase;
  private final BuscarPerfilArtistaUseCase buscarPerfilArtistaUseCase;
  private final GerarCompartilhamentoArtistaUseCase gerarCompartilhamentoArtistaUseCase;
  private final PromoverUsuarioParaArtistaUseCase promoverUsuarioParaArtistaUseCase;
  private final AtualizarDadosArtistaUseCase atualizarDadosArtistaUseCase;
  private final DesativarStatusArtistaUseCase desativarStatusArtistaUseCase;

  @PostMapping("/{id}/seguir")
  public ResponseEntity<NovoRecursoResponse> seguir(@PathVariable final UUID id) {
    seguirArtistaUseCase.execute(id, getUsuarioId());
    return ResponseEntity.ok(
        NovoRecursoResponse.builder()
            .criado(Boolean.TRUE)
            .criadoEm(LocalDate.now())
            .mensagem("Você agora está seguindo este artista!")
            .build());
  }

  @PostMapping("/{id}/bloquear")
  public ResponseEntity<NovoRecursoResponse> bloquear(@PathVariable final UUID id) {
    bloquearArtistaUseCase.execute(id, getUsuarioId());
    return ResponseEntity.ok(
        NovoRecursoResponse.builder()
            .criado(Boolean.TRUE)
            .criadoEm(LocalDate.now())
            .mensagem("Artista bloqueado com sucesso!")
            .build());
  }

  @GetMapping("/{id}/discografia")
  public ResponseEntity<ArtistaDiscografiaResponse> discografia(@PathVariable final UUID id) {
    return ResponseEntity.ok(consultarDiscografiaArtistaUseCase.execute(id));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ArtistaPerfilResponse> perfil(@PathVariable final UUID id) {
    return ResponseEntity.ok(buscarPerfilArtistaUseCase.execute(id));
  }

  @GetMapping("/{id}/compartilhamento")
  public ResponseEntity<ArtistaCompartilhamentoResponse> compartilhamento(
      @PathVariable final UUID id) {
    return ResponseEntity.ok(gerarCompartilhamentoArtistaUseCase.execute(id));
  }

  @PostMapping("/{id}/promover")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<RecursoAtualizadoResponse> promover(@PathVariable final UUID id) {
    promoverUsuarioParaArtistaUseCase.execute(id);
    return ResponseEntity.ok(
        RecursoAtualizadoResponse.builder()
            .atualizado(Boolean.TRUE)
            .atualizadoEm(LocalDate.now())
            .mensagem("Usuário promovido a artista com sucesso!")
            .build());
  }

  @PatchMapping("/{id}")
  public ResponseEntity<RecursoAtualizadoResponse> atualizarDados(
      @PathVariable final UUID id, @RequestBody @Valid final AtualizarDadosArtistaRequest request) {
    atualizarDadosArtistaUseCase.execute(id, request, getUsuarioId());
    return ResponseEntity.ok(
        RecursoAtualizadoResponse.builder()
            .atualizado(Boolean.TRUE)
            .atualizadoEm(LocalDate.now())
            .mensagem("Dados de artista atualizados com sucesso!")
            .build());
  }

  @DeleteMapping("/{id}/status")
  public ResponseEntity<RecursoDeletadoResponse> desativarStatus(@PathVariable final UUID id) {
    desativarStatusArtistaUseCase.execute(id, getUsuarioId());
    return ResponseEntity.ok(
        RecursoDeletadoResponse.builder()
            .deletado(Boolean.TRUE)
            .deletadoEm(LocalDate.now())
            .mensagem("Status de artista desativado com sucesso!")
            .build());
  }

  private UUID getUsuarioId() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return ((UsuarioDetalhesImpl) authentication.getPrincipal()).getId();
  }
}
