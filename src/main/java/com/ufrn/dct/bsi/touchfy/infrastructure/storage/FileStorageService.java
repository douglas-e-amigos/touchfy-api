package com.ufrn.dct.bsi.touchfy.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoArmazenamentoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoRecuperadoResponse;

public interface FileStorageService {
    ArquivoArmazenamentoResponse store(MultipartFile file, String subDirectory);
    void delete(String caminhoDoArquivo);
    String getUrl(String caminhoDoArquivo);
    ArquivoRecuperadoResponse retrieve(String caminhoDoArquivo);
}
