package com.ufrn.dct.bsi.touchfy.domain.musica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.Tag;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class TagTest {

  @Test
  void deveCriarTagQuandoDadosForemValidos() {
    final UUID id = UUID.randomUUID();
    final Tag tag = new Tag(id, "rock");

    assertEquals(id, tag.getId());
    assertEquals("rock", tag.getNome());
  }

  @Test
  void deveLancarExcecaoQuandoIdForNulo() {
    final IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Tag(null, "rock"));

    assertEquals("ID é obrigatório", exception.getMessage());
  }

  @Test
  void deveLancarExcecaoQuandoNomeForNulo() {
    final IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Tag(UUID.randomUUID(), null));

    assertEquals("Nome é obrigatório", exception.getMessage());
  }
}
