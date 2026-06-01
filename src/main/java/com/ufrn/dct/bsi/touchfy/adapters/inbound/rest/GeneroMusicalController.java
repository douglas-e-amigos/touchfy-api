package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarGeneroMusicalRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarGeneroMusicalRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero.AtualizarGeneroMusicalUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero.ConsultarGenerosMusicaisUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero.CriarGeneroMusicalUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero.DeletarGeneroMusicalUseCase;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.GeneroMusical;
import com.ufrn.dct.bsi.touchfy.shared.dtos.NovoRecursoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoAtualizadoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.RecursoDeletadoResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/musicas/generos-musicais")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class GeneroMusicalController {
    private final CriarGeneroMusicalUseCase criarGeneroMusicalUseCase;
    private final AtualizarGeneroMusicalUseCase atualizarGeneroMusicalUseCase;
    private final ConsultarGenerosMusicaisUseCase consultarGenerosMusicaisUseCase;
    private final DeletarGeneroMusicalUseCase deletarGeneroMusicalUseCase;

    @PostMapping
    public ResponseEntity<NovoRecursoResponse> criarGeneroMusical(
            @RequestBody @Valid final CriarGeneroMusicalRequest request
    ) {
        criarGeneroMusicalUseCase.execute(request);
        return ResponseEntity.ok(NovoRecursoResponse.builder()
                .criado(Boolean.TRUE)
                .criadoEm(LocalDate.now())
                .mensagem("Gênero musical criado com sucesso!")
                .build()
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RecursoAtualizadoResponse> atualizarGeneroMusical(
            @PathVariable("id") final UUID id,
            @RequestBody @Valid final AtualizarGeneroMusicalRequest request
    ) {
        atualizarGeneroMusicalUseCase.execute(id, request);

        return ResponseEntity.ok(RecursoAtualizadoResponse.builder()
                .atualizado(Boolean.TRUE)
                .atualizadoEm(LocalDate.now())
                .mensagem("Gênero musical atualizado com sucesso!")
                .build()
        );
    }

    @GetMapping
    public ResponseEntity<List<GeneroMusical>> consultarGenerosMusicais() {
        final var generosMusicais = consultarGenerosMusicaisUseCase.execute();
        return ResponseEntity.ok(generosMusicais);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RecursoDeletadoResponse> deletarGeneroMusical(@PathVariable("id") final UUID id) {
        deletarGeneroMusicalUseCase.execute(id);
        return ResponseEntity.ok(new RecursoDeletadoResponse(
                        "Gênero musical deletado com sucesso!",
                        Boolean.TRUE,
                        LocalDate.now()
                )
        );
    }
}