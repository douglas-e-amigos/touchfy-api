package com.ufrn.dct.bsi.touchfy.domain.album.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class AlbumSalvo {
    private UUID id;
    private Album album;
    private UUID usuarioId;

    public AlbumSalvo(final UUID id, final Album album, final UUID usuarioId) {
        if (id == null) {
            throw new IllegalArgumentException("ID é obrigatório");
        }
        if (album == null) {
            throw new IllegalArgumentException("Álbum é obrigatório");
        }
        if (usuarioId == null) {
            throw new IllegalArgumentException("Usuário é obrigatório");
        }

        this.id = id;
        this.album = album;
        this.usuarioId = usuarioId;
    }
}
