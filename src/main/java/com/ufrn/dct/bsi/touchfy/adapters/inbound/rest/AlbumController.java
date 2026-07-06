package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.security.UsuarioDetalhesImpl;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AdicionarMusicaAlbumRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumSalvoResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AtualizarAlbumRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.CriarAlbumRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.album.AdicionarMusicaAlbumUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.album.AtualizarAlbumUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.album.BuscarAlbumUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.album.CriarAlbumUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.album.DeletarAlbumUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.album.ListarAlbunsSalvosUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.album.ListarAlbunsUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.album.RemoverAlbumSalvoUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.album.SalvarAlbumUseCase;
import com.ufrn.dct.bsi.touchfy.shared.dtos.NovoRecursoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoAtualizadoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoDeletadoResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/albuns")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class AlbumController {
  private final CriarAlbumUseCase criarAlbumUseCase;
  private final AtualizarAlbumUseCase atualizarAlbumUseCase;
  private final DeletarAlbumUseCase deletarAlbumUseCase;
  private final BuscarAlbumUseCase buscarAlbumUseCase;
  private final ListarAlbunsUseCase listarAlbunsUseCase;
  private final AdicionarMusicaAlbumUseCase adicionarMusicaAlbumUseCase;
  private final SalvarAlbumUseCase salvarAlbumUseCase;
  private final RemoverAlbumSalvoUseCase removerAlbumSalvoUseCase;
  private final ListarAlbunsSalvosUseCase listarAlbunsSalvosUseCase;

  @PostMapping
  @PreAuthorize("hasAuthority('album:create')")
  public ResponseEntity<NovoRecursoResponse> criarAlbum(
      @RequestBody @Valid final CriarAlbumRequest request) {
    final UUID usuarioId = getUsuarioId();
    criarAlbumUseCase.execute(request, usuarioId);
    return ResponseEntity.ok(
        NovoRecursoResponse.builder()
            .criado(Boolean.TRUE)
            .criadoEm(LocalDate.now())
            .mensagem("Álbum criado com sucesso!")
            .build());
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('album:update')")
  public ResponseEntity<RecursoAtualizadoResponse> atualizarAlbum(
      @PathVariable("id") final UUID id, @RequestBody @Valid final AtualizarAlbumRequest request) {
    final UUID usuarioId = getUsuarioId();
    atualizarAlbumUseCase.execute(id, request, usuarioId);
    return ResponseEntity.ok(
        RecursoAtualizadoResponse.builder()
            .atualizado(Boolean.TRUE)
            .atualizadoEm(LocalDate.now())
            .mensagem("Álbum atualizado com sucesso!")
            .build());
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('album:delete')")
  public ResponseEntity<RecursoDeletadoResponse> deletarAlbum(@PathVariable("id") final UUID id) {
    final UUID usuarioId = getUsuarioId();
    deletarAlbumUseCase.execute(id, usuarioId);
    return ResponseEntity.ok(
        RecursoDeletadoResponse.builder()
            .deletado(Boolean.TRUE)
            .deletadoEm(LocalDate.now())
            .mensagem("Álbum deletado com sucesso!")
            .build());
  }

  @GetMapping("/{id}")
  public ResponseEntity<AlbumResponse> buscarAlbum(@PathVariable("id") final UUID id) {
    return ResponseEntity.ok(buscarAlbumUseCase.execute(id));
  }

  @GetMapping
  public ResponseEntity<List<AlbumResponse>> listarAlbuns() {
    return ResponseEntity.ok(listarAlbunsUseCase.execute());
  }

  @PostMapping("/{id}/musicas")
  @PreAuthorize("hasAuthority('album:update')")
  public ResponseEntity<NovoRecursoResponse> adicionarMusica(
      @PathVariable("id") final UUID id,
      @RequestBody @Valid final AdicionarMusicaAlbumRequest request) {
    final UUID usuarioId = getUsuarioId();
    adicionarMusicaAlbumUseCase.execute(id, request.musicaId(), usuarioId);
    return ResponseEntity.ok(
        NovoRecursoResponse.builder()
            .criado(Boolean.TRUE)
            .criadoEm(LocalDate.now())
            .mensagem("Música adicionada ao álbum com sucesso!")
            .build());
  }

  @PostMapping("/{id}/salvar")
  public ResponseEntity<NovoRecursoResponse> salvarAlbum(@PathVariable("id") final UUID id) {
    final UUID usuarioId = getUsuarioId();
    salvarAlbumUseCase.execute(id, usuarioId);
    return ResponseEntity.ok(
        NovoRecursoResponse.builder()
            .criado(Boolean.TRUE)
            .criadoEm(LocalDate.now())
            .mensagem("Álbum salvo com sucesso!")
            .build());
  }

  @DeleteMapping("/{id}/salvar")
  public ResponseEntity<RecursoDeletadoResponse> removerAlbumSalvo(
      @PathVariable("id") final UUID id) {
    final UUID usuarioId = getUsuarioId();
    removerAlbumSalvoUseCase.execute(id, usuarioId);
    return ResponseEntity.ok(
        RecursoDeletadoResponse.builder()
            .deletado(Boolean.TRUE)
            .deletadoEm(LocalDate.now())
            .mensagem("Álbum removido dos salvos com sucesso!")
            .build());
  }

  @GetMapping("/salvos")
  public ResponseEntity<List<AlbumSalvoResponse>> listarAlbunsSalvos() {
    final UUID usuarioId = getUsuarioId();
    return ResponseEntity.ok(listarAlbunsSalvosUseCase.execute(usuarioId));
  }

  private UUID getUsuarioId() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return ((UsuarioDetalhesImpl) authentication.getPrincipal()).getId();
  }
}
