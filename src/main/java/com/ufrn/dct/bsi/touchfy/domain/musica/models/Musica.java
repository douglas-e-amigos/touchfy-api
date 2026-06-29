package com.ufrn.dct.bsi.touchfy.domain.musica.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class Musica {
    private UUID id;
    private String nome;
    private String caminhoDoArquivo;
    private String letra;
    private UUID criadoPor;
    private String artistaNome;
    private String artistaNomeUsuario;
    private List<Tag> tags;
    private List<GeneroMusical> generosMusicais;

    @Builder
    public Musica(
            final UUID id,
            final String nome,
            final String caminhoDoArquivo,
            final String letra,
            final UUID criadoPor,
            final String artistaNome,
            final String artistaNomeUsuario,
            final List<Tag> tags,
            final List<GeneroMusical> generosMusicais
    ) {
        if (id == null) {
            throw new IllegalArgumentException("ID é obrigatório");
        }

        if (nome == null) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (caminhoDoArquivo == null) {
            throw new IllegalArgumentException("Caminho do arquivo é obrigatório");
        }

        this.id = id;
        this.nome = nome;
        this.caminhoDoArquivo = caminhoDoArquivo;
        this.letra = letra;
        this.criadoPor = criadoPor;
        this.artistaNome = artistaNome;
        this.artistaNomeUsuario = artistaNomeUsuario;
        this.tags = tags == null ? List.of() : tags;
        this.generosMusicais = generosMusicais == null ? List.of() : generosMusicais;
    }

    public Musica(
            final UUID id,
            final String nome,
            final String caminhoDoArquivo,
            final String letra,
            final List<Tag> tags,
            final List<GeneroMusical> generosMusicais
    ) {
        this(id, nome, caminhoDoArquivo, letra, null, null, null, tags, generosMusicais);
    }
}
