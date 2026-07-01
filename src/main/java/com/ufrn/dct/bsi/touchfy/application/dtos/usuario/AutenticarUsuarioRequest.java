package com.ufrn.dct.bsi.touchfy.application.dtos.usuario;

import jakarta.validation.constraints.NotBlank;

public record AutenticarUsuarioRequest(
    @NotBlank(message = "O nome de usuário é obrigatório.") String nomeUsuario,
    @NotBlank(message = "A senha é obrigatória.") String senha) {}
