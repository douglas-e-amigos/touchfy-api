package com.ufrn.dct.bsi.touchfy.domain.musica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class MusicaTest {

  @Test
  void deveCriarMusicaQuandoDadosForemValidos() {
    final UUID id = UUID.randomUUID();
    final Musica musica =
        new Musica(id, "Tempo Perdido", "musicas/abc/arquivo.mp3", "Letra", null, null);

    assertEquals(id, musica.getId());
    assertEquals("Tempo Perdido", musica.getNome());
    assertEquals("musicas/abc/arquivo.mp3", musica.getCaminhoDoArquivo());
    assertEquals("Letra", musica.getLetra());
    assertTrue(musica.getTags().isEmpty());
    assertTrue(musica.getGenerosMusicais().isEmpty());
  }

  @Test
  void deveLancarExcecaoQuandoIdForNulo() {
    final IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                new Musica(
                    null, "Tempo Perdido", "musicas/abc/arquivo.mp3", null, List.of(), List.of()));

    assertEquals("ID é obrigatório", exception.getMessage());
  }

  @Test
  void deveLancarExcecaoQuandoNomeForNulo() {
    final IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () ->
                new Musica(
                    UUID.randomUUID(),
                    null,
                    "musicas/abc/arquivo.mp3",
                    null,
                    List.of(),
                    List.of()));

    assertEquals("Nome é obrigatório", exception.getMessage());
  }

  @Test
  void deveLancarExcecaoQuandoCaminhoDoArquivoForNulo() {
    final IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> new Musica(UUID.randomUUID(), "Tempo Perdido", null, null, List.of(), List.of()));

    assertEquals("Caminho do arquivo é obrigatório", exception.getMessage());
  }
}
