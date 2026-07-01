package com.ufrn.dct.bsi.touchfy.domain.album.models;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.GeneroMusical;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class Album {
    private UUID id;
    private String nome;
    private String descricao;
    private LocalDate dataLancamento;
    private GeneroMusical generoMusical;
    private UUID artistaId;
    private List<Musica> musicas;

    public Album(final UUID id, final String nome, final String descricao,
                 final LocalDate dataLancamento, final GeneroMusical generoMusical,
                 final UUID artistaId, final List<Musica> musicas) {
        if (id == null) {
            throw new IllegalArgumentException("ID é obrigatório");
        }
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        if (artistaId == null) {
            throw new IllegalArgumentException("Artista é obrigatório");
        }

        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.dataLancamento = dataLancamento;
        this.generoMusical = generoMusical;
        this.artistaId = artistaId;
        this.musicas = musicas == null ? List.of() : musicas;
    }
}
