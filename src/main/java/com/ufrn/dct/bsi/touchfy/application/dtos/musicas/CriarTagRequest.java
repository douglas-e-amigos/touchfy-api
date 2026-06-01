package com.ufrn.dct.bsi.touchfy.application.dtos.musicas;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarTagRequest(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(min = 2, max = 100, message = "O nome da tag deve ter entre 2 e 100 caracteres.")
        String nome
) {
}
