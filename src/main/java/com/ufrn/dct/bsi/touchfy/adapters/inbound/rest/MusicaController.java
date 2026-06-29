package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarMusicaRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarMusicaRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.MusicaResponse;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.StreamingMusicaResponse;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.AtualizarMusicaUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.BuscarMusicaUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.ConsultarMusicasDoArtistaUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.ConsultarMusicasUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.CriarMusicaUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.DeletarMusicaUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.StreamingMusicaUseCase;
import com.ufrn.dct.bsi.touchfy.shared.dtos.NovoRecursoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoAtualizadoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoDeletadoResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/musicas")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MusicaController {
    private final CriarMusicaUseCase criarMusicaUseCase;
    private final AtualizarMusicaUseCase atualizarMusicaUseCase;
    private final BuscarMusicaUseCase buscarMusicaUseCase;
    private final ConsultarMusicasDoArtistaUseCase consultarMusicasDoArtistaUseCase;
    private final ConsultarMusicasUseCase consultarMusicasUseCase;
    private final DeletarMusicaUseCase deletarMusicaUseCase;
    private final StreamingMusicaUseCase streamingMusicaUseCase;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('music:create')")
    public ResponseEntity<NovoRecursoResponse> criarMusica(@ModelAttribute @Valid final CriarMusicaRequest request) {
        criarMusicaUseCase.execute(request);
        return ResponseEntity.ok(NovoRecursoResponse.builder()
                .criado(Boolean.TRUE)
                .criadoEm(LocalDate.now())
                .mensagem("Música criada com sucesso!")
                .build());
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('music:update')")
    public ResponseEntity<RecursoAtualizadoResponse> atualizarMusica(
            @PathVariable("id") final UUID id,
            @ModelAttribute @Valid final AtualizarMusicaRequest request) {
        atualizarMusicaUseCase.execute(id, request);

        return ResponseEntity.ok(RecursoAtualizadoResponse.builder()
                .atualizado(Boolean.TRUE)
                .atualizadoEm(LocalDate.now())
                .mensagem("Música atualizada com sucesso!")
                .build());
    }

    @GetMapping
    public ResponseEntity<List<MusicaResponse>> consultarMusicas(
            @RequestParam(value = "artista", required = false) final String artista) {
        return ResponseEntity.ok(consultarMusicasUseCase.execute(artista));
    }

    @GetMapping("/artista/{artistaId}")
    @PreAuthorize("hasAuthority('music:read')")
    public ResponseEntity<List<MusicaResponse>> consultarMusicasDoArtista(
            @PathVariable("artistaId") final UUID artistaId) {
        return ResponseEntity.ok(consultarMusicasDoArtistaUseCase.execute(artistaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MusicaResponse> buscarMusica(@PathVariable("id") final UUID id) {
        return ResponseEntity.ok(buscarMusicaUseCase.execute(id));
    }

        @Operation(summary = "Realiza o streaming parcial de uma música em MP3")
        @ApiResponses(value = {
            @ApiResponse(
                responseCode = "200",
                description = "Áudio completo retornado com sucesso",
                content = @Content(mediaType = "audio/mpeg", schema = @Schema(type = "string", format = "binary"))
            ),
            @ApiResponse(
                responseCode = "206",
                description = "Faixa parcial do áudio retornada com sucesso",
                content = @Content(mediaType = "audio/mpeg", schema = @Schema(type = "string", format = "binary"))
            )
        })

    @GetMapping(value = "/stream/{id}", produces = "audio/mpeg")
    @PreAuthorize("hasAuthority('music:read')")
    public ResponseEntity<byte[]> streamMusica(
            @PathVariable("id") final UUID id,
            @RequestHeader(value = HttpHeaders.RANGE, required = false) final String range) {
        final StreamingMusicaResponse response = streamingMusicaUseCase.execute(id, range);
        final MediaType mediaType = response.contentType() == null
                ? MediaType.APPLICATION_OCTET_STREAM
                : MediaType.parseMediaType(response.contentType());

        final var responseBuilder = ResponseEntity
                .status(response.parcial() ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
                .contentType(mediaType)
                .contentLength(response.conteudo().length)
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + response.nome() + "\"");

        if (response.parcial()) {
            responseBuilder.header(
                    HttpHeaders.CONTENT_RANGE,
                    "bytes " + response.inicio() + "-" + response.fim() + "/" + response.tamanhoTotal());
        }

        return responseBuilder.body(response.conteudo());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('music:delete')")
    public ResponseEntity<RecursoDeletadoResponse> deletarMusica(@PathVariable("id") final UUID id) {
        deletarMusicaUseCase.execute(id);
        return ResponseEntity.ok(new RecursoDeletadoResponse(
                "Música deletada com sucesso!",
                Boolean.TRUE,
                LocalDate.now()));
    }
}
