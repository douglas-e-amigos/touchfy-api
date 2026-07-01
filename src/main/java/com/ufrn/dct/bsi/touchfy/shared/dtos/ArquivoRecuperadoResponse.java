package com.ufrn.dct.bsi.touchfy.shared.dtos;

public record ArquivoRecuperadoResponse(
    String nome, String caminhoDoArquivo, String contentType, byte[] conteudo) {}
