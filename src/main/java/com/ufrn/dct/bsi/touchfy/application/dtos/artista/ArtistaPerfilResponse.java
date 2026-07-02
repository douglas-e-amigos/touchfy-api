package com.ufrn.dct.bsi.touchfy.application.dtos.artista;

import java.util.UUID;
import lombok.Builder;

@Builder
public record ArtistaPerfilResponse(
    UUID id,
    String nome,
    String nomeUsuario,
    String descricao,
    String imagem,
    long numeroDeOuvintes,
    Integer posicaoRanking) {}
