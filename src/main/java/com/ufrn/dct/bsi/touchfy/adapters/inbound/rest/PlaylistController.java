package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.security.UsuarioDetalhesImpl;
import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.AdicionarMusicaPlaylistRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.AtualizarPlaylistRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.CriarPlaylistRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.PlaylistResponse;
import com.ufrn.dct.bsi.touchfy.application.usecases.playlist.AdicionarMusicaPlaylistUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.playlist.AtualizarPlaylistUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.playlist.BuscarPlaylistUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.playlist.CriarPlaylistUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.playlist.DeletarPlaylistUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.playlist.ListarPlaylistsUsuarioUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.playlist.RemoverMusicaPlaylistUseCase;
import com.ufrn.dct.bsi.touchfy.shared.dtos.NovoRecursoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoAtualizadoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoDeletadoResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/playlists")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PlaylistController {
    private final CriarPlaylistUseCase criarPlaylistUseCase;
    private final AtualizarPlaylistUseCase atualizarPlaylistUseCase;
    private final DeletarPlaylistUseCase deletarPlaylistUseCase;
    private final BuscarPlaylistUseCase buscarPlaylistUseCase;
    private final ListarPlaylistsUsuarioUseCase listarPlaylistsUsuarioUseCase;
    private final AdicionarMusicaPlaylistUseCase adicionarMusicaPlaylistUseCase;
    private final RemoverMusicaPlaylistUseCase removerMusicaPlaylistUseCase;

    @PostMapping
    public ResponseEntity<NovoRecursoResponse> criarPlaylist(
            @RequestBody @Valid final CriarPlaylistRequest request) {
        final UUID usuarioId = getUsuarioId();
        criarPlaylistUseCase.execute(request, usuarioId);
        return ResponseEntity.ok(NovoRecursoResponse.builder()
                .criado(Boolean.TRUE)
                .criadoEm(LocalDate.now())
                .mensagem("Playlist criada com sucesso!")
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecursoAtualizadoResponse> atualizarPlaylist(
            @PathVariable("id") final UUID id,
            @RequestBody @Valid final AtualizarPlaylistRequest request) {
        final UUID usuarioId = getUsuarioId();
        atualizarPlaylistUseCase.execute(id, request, usuarioId);
        return ResponseEntity.ok(RecursoAtualizadoResponse.builder()
                .atualizado(Boolean.TRUE)
                .atualizadoEm(LocalDate.now())
                .mensagem("Playlist atualizada com sucesso!")
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RecursoDeletadoResponse> deletarPlaylist(@PathVariable("id") final UUID id) {
        final UUID usuarioId = getUsuarioId();
        deletarPlaylistUseCase.execute(id, usuarioId);
        return ResponseEntity.ok(RecursoDeletadoResponse.builder()
                .deletado(Boolean.TRUE)
                .deletadoEm(LocalDate.now())
                .mensagem("Playlist deletada com sucesso!")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistResponse> buscarPlaylist(@PathVariable("id") final UUID id) {
        final UUID usuarioId = getUsuarioId();
        return ResponseEntity.ok(buscarPlaylistUseCase.execute(id, usuarioId));
    }

    @GetMapping
    public ResponseEntity<List<PlaylistResponse>> listarPlaylists() {
        final UUID usuarioId = getUsuarioId();
        return ResponseEntity.ok(listarPlaylistsUsuarioUseCase.execute(usuarioId));
    }

    @PostMapping("/{id}/musicas")
    public ResponseEntity<NovoRecursoResponse> adicionarMusica(
            @PathVariable("id") final UUID id,
            @RequestBody @Valid final AdicionarMusicaPlaylistRequest request) {
        final UUID usuarioId = getUsuarioId();
        adicionarMusicaPlaylistUseCase.execute(id, request.musicaId(), usuarioId);
        return ResponseEntity.ok(NovoRecursoResponse.builder()
                .criado(Boolean.TRUE)
                .criadoEm(LocalDate.now())
                .mensagem("Música adicionada à playlist com sucesso!")
                .build());
    }

    @DeleteMapping("/{id}/musicas/{musicaId}")
    public ResponseEntity<RecursoDeletadoResponse> removerMusica(
            @PathVariable("id") final UUID id,
            @PathVariable("musicaId") final UUID musicaId) {
        final UUID usuarioId = getUsuarioId();
        removerMusicaPlaylistUseCase.execute(id, musicaId, usuarioId);
        return ResponseEntity.ok(RecursoDeletadoResponse.builder()
                .deletado(Boolean.TRUE)
                .deletadoEm(LocalDate.now())
                .mensagem("Música removida da playlist com sucesso!")
                .build());
    }

    private UUID getUsuarioId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((UsuarioDetalhesImpl) authentication.getPrincipal()).getId();
    }
}
