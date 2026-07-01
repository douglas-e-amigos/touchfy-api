package com.ufrn.dct.bsi.touchfy.domain.album.models;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.GeneroMusical;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AlbumSalvoTest {

    @Test
    void deveCriarAlbumSalvoQuandoDadosForemValidos() {
        final var id = UUID.randomUUID();
        final var usuarioId = UUID.randomUUID();
        final var album = new Album(UUID.randomUUID(), "Album", null, null, null,
                UUID.randomUUID(), null);
        final var salvo = new AlbumSalvo(id, album, usuarioId);

        assertEquals(id, salvo.getId());
        assertEquals(album, salvo.getAlbum());
        assertEquals(usuarioId, salvo.getUsuarioId());
    }

    @Test
    void deveLancarExcecaoQuandoIdForNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new AlbumSalvo(null, new Album(UUID.randomUUID(), "A", null, null, null,
                        UUID.randomUUID(), null), UUID.randomUUID()));
    }

    @Test
    void deveLancarExcecaoQuandoAlbumForNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new AlbumSalvo(UUID.randomUUID(), null, UUID.randomUUID()));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioForNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new AlbumSalvo(UUID.randomUUID(), new Album(UUID.randomUUID(), "A", null, null, null,
                        UUID.randomUUID(), null), null));
    }
}
