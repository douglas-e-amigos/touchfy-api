package com.ufrn.dct.bsi.touchfy.domain.playlist.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class PlaylistTest {

  @Test
  void deveCriarPlaylistQuandoDadosForemValidos() {
    final var id = UUID.randomUUID();
    final var donoId = UUID.randomUUID();
    final var playlist =
        new Playlist(
            id, "Minha Playlist", "Descrição", Visibilidade.PUBLICA, donoId, List.of(), List.of());

    assertEquals(id, playlist.getId());
    assertEquals("Minha Playlist", playlist.getNome());
    assertEquals("Descrição", playlist.getDescricao());
    assertEquals(Visibilidade.PUBLICA, playlist.getVisibilidade());
    assertEquals(donoId, playlist.getDonoId());
  }

  @Test
  void deveLancarExcecaoQuandoIdForNulo() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new Playlist(
                null, "Nome", null, Visibilidade.PUBLICA, UUID.randomUUID(), List.of(), List.of()));
  }

  @Test
  void deveLancarExcecaoQuandoNomeForNulo() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new Playlist(
                UUID.randomUUID(),
                null,
                null,
                Visibilidade.PUBLICA,
                UUID.randomUUID(),
                List.of(),
                List.of()));
  }

  @Test
  void deveLancarExcecaoQuandoNomeForVazio() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new Playlist(
                UUID.randomUUID(),
                "",
                null,
                Visibilidade.PUBLICA,
                UUID.randomUUID(),
                List.of(),
                List.of()));
  }

  @Test
  void deveLancarExcecaoQuandoVisibilidadeForNula() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new Playlist(
                UUID.randomUUID(), "Nome", null, null, UUID.randomUUID(), List.of(), List.of()));
  }

  @Test
  void deveLancarExcecaoQuandoDonoForNulo() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new Playlist(
                UUID.randomUUID(), "Nome", null, Visibilidade.PUBLICA, null, List.of(), List.of()));
  }

  @Test
  void deveInicializarListasVaziasQuandoMusicasForNulo() {
    final var playlist =
        new Playlist(
            UUID.randomUUID(), "Nome", null, Visibilidade.PUBLICA, UUID.randomUUID(), null, null);

    assertNotNull(playlist.getMusicas());
    assertTrue(playlist.getMusicas().isEmpty());
    assertNotNull(playlist.getUsuariosConvidadosIds());
    assertTrue(playlist.getUsuariosConvidadosIds().isEmpty());
  }
}
