package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.DeletarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.UploadArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoArmazenamentoResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/arquivos")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ArquivoController {

    private final UploadArquivoUseCase uploadArquivoUseCase;
    private final DeletarArquivoUseCase deletarArquivoUseCase;

    @PostMapping(
            value = "/upload/{subDirectory}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ArquivoArmazenamentoResponse> upload(
            @PathVariable final String subDirectory,
            @RequestPart("file") final MultipartFile file
    ) {
        final ArquivoArmazenamentoResponse response = uploadArquivoUseCase.execute(file, subDirectory);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{subDirectory}/{nomeDoArquivo}")
    public ResponseEntity<Void> deletar(
            @PathVariable final String subDirectory,
            @PathVariable final String nomeDoArquivo
    ) {
        deletarArquivoUseCase.execute(subDirectory + "/" + nomeDoArquivo);
        return ResponseEntity.noContent().build();
    }
}