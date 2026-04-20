package com.ufrn.dct.bsi.touchfy.domain.user.models;

import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import com.ufrn.dct.bsi.touchfy.shared.models.Imagem;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class Usuario {
    private UUID id;
    private String nome;
    private String nomeUsuario;
    private String senha;
    private Email email;
    private Imagem imagem;
    private Boolean emailVerificado;
    private Date dataNascimento;

    public Usuario(
            final UUID id,
            final String nome,
            final String nomeUsuario,
            final String senha,
            final Email email,
            final Imagem imagem,
            final Boolean emailVerificado,
            final Date dataNascimento
    ) {
        if (id == null)
            throw new IllegalArgumentException("ID é obrigatório");
        if (nome == null)
            throw new IllegalArgumentException("Nome é obrigatório");
        if (nomeUsuario == null)
            throw new IllegalArgumentException("Nome de usuário é obrigatório");
        if (senha == null)
            throw new IllegalArgumentException("Senha é obrigatória");
        if (email == null)
            throw new IllegalArgumentException("E-mail é obrigatório");
        if (emailVerificado == null)
            throw new IllegalArgumentException("E-mail verificado é obrigatório");
        if (dataNascimento == null)
            throw new IllegalArgumentException("Data nascimento é obrigatória");

        this.id = id;
        this.nome = nome;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.email = email;
        this.imagem = imagem;
        this.emailVerificado = emailVerificado;
        this.dataNascimento = dataNascimento;
    }
}
