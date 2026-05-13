package com.ufrn.dct.bsi.touchfy.application.usecases.arquivo;

import org.springframework.stereotype.Component;

import com.ufrn.dct.bsi.touchfy.infrastructure.storage.FileStorageService;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoRecuperadoResponse;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class BuscarArquivoUseCase {

    private final FileStorageService fileStorageService;

    public ArquivoRecuperadoResponse execute(final String caminhoDoArquivo) {
        return fileStorageService.retrieve(caminhoDoArquivo);
    }
}