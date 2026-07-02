package com.ufrn.dct.bsi.touchfy.application.dtos.artista;

import jakarta.validation.constraints.Size;

public record AtualizarDadosArtistaRequest(
    @Size(min = 1, max = 255, message = "Nome deve ter entre 1 e 255 caracteres") String nome,
    @Size(max = 2000, message = "Descrição deve ter no máximo 2000 caracteres") String descricao) {}
