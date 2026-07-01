package com.ufrn.dct.bsi.touchfy.application.usecases.arquivo;

import com.ufrn.dct.bsi.touchfy.infrastructure.storage.FileStorageService;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoRecuperadoResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BuscarArquivoUseCase {

  private final FileStorageService fileStorageService;

  public ArquivoRecuperadoResponse execute(final String caminhoDoArquivo) {
    return fileStorageService.retrieve(caminhoDoArquivo);
  }
}
