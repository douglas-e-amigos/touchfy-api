package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarTagRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarTagRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag.AtualizarTagUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag.ConsultarTagsUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag.CriarTagUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag.DeletarTagUseCase;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.Tag;
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
import java.util.UUID;

@RestController
@RequestMapping("/musicas/tags")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TagController {
    private final CriarTagUseCase criarTagUseCase;
    private final AtualizarTagUseCase atualizarTagUseCase;
    private final ConsultarTagsUseCase consultarTagsUseCase;
    private final DeletarTagUseCase deletarTagUseCase;

    @PostMapping
    public ResponseEntity<NovoRecursoResponse> criarTag(@RequestBody @Valid final CriarTagRequest request) {
        criarTagUseCase.execute(request);
        return ResponseEntity.ok(NovoRecursoResponse.builder()
                .criado(Boolean.TRUE)
                .criadoEm(LocalDate.now())
                .mensagem("Tag criada com sucesso!")
                .build()
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecursoAtualizadoResponse> atualizarTag(
            @PathVariable("id") final UUID id,
            @RequestBody final AtualizarTagRequest request
    ) {
        atualizarTagUseCase.execute(id, request);

        return ResponseEntity.ok(RecursoAtualizadoResponse.builder()
                .atualizado(Boolean.TRUE)
                .atualizadoEm(LocalDate.now())
                .mensagem("Tag atualizada com sucesso!")
                .build()
        );
    }

    @GetMapping
    public ResponseEntity<List<Tag>> consultarTags() {
        final var tags = consultarTagsUseCase.execute();
        return ResponseEntity.ok(tags);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RecursoDeletadoResponse> deletarTag(@PathVariable("id") final UUID id) {
        deletarTagUseCase.execute(id);
        return ResponseEntity.ok(new RecursoDeletadoResponse(
                        "Tag deletada com sucesso!",
                        Boolean.TRUE,
                        LocalDate.now()
                )
        );
    }
}
