package com.ufrn.dct.bsi.touchfy.application.dtos.artista;

import lombok.Builder;

@Builder
public record ArtistaCompartilhamentoResponse(String link, String nomeArtista, String imagem) {}
