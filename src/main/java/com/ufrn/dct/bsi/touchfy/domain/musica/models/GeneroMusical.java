package com.ufrn.dct.bsi.touchfy.domain.musica.models;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GeneroMusical {
    private UUID id;
    private String nome;

    public GeneroMusical(
            final UUID id,
            final String nome
    ) {
        if (id == null) {
            throw new IllegalArgumentException("ID é obrigatório");
        }

        if (nome == null) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        this.id = id;
        this.nome = nome;
    }
}