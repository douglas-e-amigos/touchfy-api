package com.ufrn.dct.bsi.touchfy.application.dtos.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Builder;

@Builder
public record AtualizarUsuarioRequest(
    @NotBlank(message = "O nome é obrigatório.") String nome,
    @NotBlank(message = "O nome de usuário é obrigatório.") String nomeUsuario,
    @NotNull(message = "A data de nascimento é obrigatória.") LocalDate dataNascimento) {}
