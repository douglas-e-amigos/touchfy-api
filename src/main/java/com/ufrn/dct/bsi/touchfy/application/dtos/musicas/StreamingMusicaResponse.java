package com.ufrn.dct.bsi.touchfy.application.dtos.musicas;

public record StreamingMusicaResponse(
    String nome,
    String contentType,
    byte[] conteudo,
    long inicio,
    long fim,
    long tamanhoTotal,
    boolean parcial) {}
