package com.ufrn.dct.bsi.touchfy.application.dtos.usuario;

import java.util.List;

public record UsuarioResponse(
    String id,
    String nome,
    String nomeUsuario,
    String email,
    String dataNascimento,
    String fotoPerfil,
    List<String> roles) {}
