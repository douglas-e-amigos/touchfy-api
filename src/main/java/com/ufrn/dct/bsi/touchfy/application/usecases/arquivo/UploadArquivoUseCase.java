package com.ufrn.dct.bsi.touchfy.application.usecases.arquivo;

import com.ufrn.dct.bsi.touchfy.infrastructure.storage.FileStorageService;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoArmazenamentoResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@AllArgsConstructor
public class UploadArquivoUseCase {

    private final FileStorageService fileStorageService;

    public ArquivoArmazenamentoResponse execute(final MultipartFile file, final String subDirectory) {
        return fileStorageService.store(file, subDirectory);
    }
}