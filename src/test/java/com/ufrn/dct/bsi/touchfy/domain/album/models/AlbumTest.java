package com.ufrn.dct.bsi.touchfy.domain.album.models;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.GeneroMusical;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AlbumTest {

    @Test
    void deveCriarAlbumQuandoDadosForemValidos() {
        final var id = UUID.randomUUID();
        final var artistaId = UUID.randomUUID();
        final var genero = new GeneroMusical(UUID.randomUUID(), "Rock");
        final var album = new Album(id, "Meu Álbum", "Desc",
                LocalDate.of(2026, 12, 1), genero, artistaId, List.of());

        assertEquals(id, album.getId());
        assertEquals("Meu Álbum", album.getNome());
        assertEquals(artistaId, album.getArtistaId());
        assertEquals(genero, album.getGeneroMusical());
    }

    @Test
    void deveLancarExcecaoQuandoIdForNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Album(null, "Nome", null, null, null,
                        UUID.randomUUID(), List.of()));
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Album(UUID.randomUUID(), null, null, null, null,
                        UUID.randomUUID(), List.of()));
    }

    @Test
    void deveLancarExcecaoQuandoNomeForVazio() {
        assertThrows(IllegalArgumentException.class, () ->
                new Album(UUID.randomUUID(), "", null, null, null,
                        UUID.randomUUID(), List.of()));
    }

    @Test
    void deveLancarExcecaoQuandoArtistaForNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Album(UUID.randomUUID(), "Nome", null, null, null,
                        null, List.of()));
    }

    @Test
    void deveInicializarMusicasComoListaVaziaQuandoNulo() {
        final var album = new Album(UUID.randomUUID(), "Nome", null, null, null,
                UUID.randomUUID(), null);
        assertNotNull(album.getMusicas());
        assertTrue(album.getMusicas().isEmpty());
    }
}
