package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.BuscarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.DeletarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.UploadArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoArmazenamentoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoRecuperadoResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/arquivos")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ArquivoController {

  private final BuscarArquivoUseCase buscarArquivoUseCase;
  private final UploadArquivoUseCase uploadArquivoUseCase;
  private final DeletarArquivoUseCase deletarArquivoUseCase;

  @GetMapping
  @PreAuthorize("hasAuthority('file:read')")
  public ResponseEntity<byte[]> buscar(@RequestParam("caminho") final String caminho) {
    final ArquivoRecuperadoResponse arquivo = buscarArquivoUseCase.execute(caminho);
    final MediaType mediaType =
        arquivo.contentType() == null
            ? MediaType.APPLICATION_OCTET_STREAM
            : MediaType.parseMediaType(arquivo.contentType());

    return ResponseEntity.ok()
        .contentType(mediaType)
        .header("Content-Disposition", "inline; filename=\"" + arquivo.nome() + "\"")
        .body(arquivo.conteudo());
  }

  @PostMapping(value = "/upload/{subDirectory}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("hasAuthority('file:upload')")
  public ResponseEntity<ArquivoArmazenamentoResponse> upload(
      @PathVariable final String subDirectory, @RequestPart("file") final MultipartFile file) {
    final ArquivoArmazenamentoResponse response = uploadArquivoUseCase.execute(file, subDirectory);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{subDirectory}/{nomeDoArquivo}")
  @PreAuthorize("hasAuthority('file:delete')")
  public ResponseEntity<Void> deletar(
      @PathVariable final String subDirectory, @PathVariable final String nomeDoArquivo) {
    deletarArquivoUseCase.execute(subDirectory + "/" + nomeDoArquivo);
    return ResponseEntity.noContent().build();
  }
}
