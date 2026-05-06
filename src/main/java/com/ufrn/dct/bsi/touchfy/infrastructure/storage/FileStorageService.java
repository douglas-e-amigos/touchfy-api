package com.ufrn.dct.bsi.touchfy.infrastructure.storage;

import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoArmazenamentoResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    ArquivoArmazenamentoResponse store(MultipartFile file, String subDirectory);
    void delete(String caminhoDoArquivo);
    String getUrl(String caminhoDoArquivo);
}
