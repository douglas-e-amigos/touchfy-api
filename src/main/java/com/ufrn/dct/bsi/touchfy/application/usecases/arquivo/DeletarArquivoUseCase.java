package com.ufrn.dct.bsi.touchfy.application.usecases.arquivo;

import com.ufrn.dct.bsi.touchfy.infrastructure.storage.FileStorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeletarArquivoUseCase {

    private final FileStorageService fileStorageService;

    public void execute(final String caminhoDoArquivo) {
        fileStorageService.delete(caminhoDoArquivo);
    }
}