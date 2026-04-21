package com.ufrn.dct.bsi.touchfy.application.dtos.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CriarUsuarioRequest(
        @NotBlank(message = "O nome é obrigatório.")
        String nome,

        @NotBlank(message = "O nome de usuário é obrigatório.")
        String nomeUsuario,

        @NotBlank(message = "A senha é obrigatória")
        String senha,

        @NotBlank(message = "A senha é obrigatória.")
        String senhaNovamente,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail está inválido.")
        String email,

        @NotNull(message = "A data de nascimento é obrigatória.")
        LocalDate dataNascimento
) {
}
