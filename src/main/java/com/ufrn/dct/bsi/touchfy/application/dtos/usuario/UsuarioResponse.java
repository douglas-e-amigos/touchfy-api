package com.ufrn.dct.bsi.touchfy.application.dtos.usuario;

public record UsuarioResponse(
        String id,
        String nome,
        String nomeUsuario,
        String email,
        String dataNascimento,
        String fotoPerfil
) {
}
