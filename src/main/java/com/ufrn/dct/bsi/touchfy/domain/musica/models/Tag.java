package com.ufrn.dct.bsi.touchfy.domain.musica.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class Tag {
    private UUID id;
    private String nome;

    public Tag(
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
