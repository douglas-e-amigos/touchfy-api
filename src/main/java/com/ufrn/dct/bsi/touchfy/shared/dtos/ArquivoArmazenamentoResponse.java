package com.ufrn.dct.bsi.touchfy.shared.dtos;

public record ArquivoArmazenamentoResponse(
        String nome,
        String caminhoDoArquivo,
        String extensao,
        Double tamanhoEmBytes
) {
}
