package com.ufrn.dct.bsi.touchfy.application.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TamanhoArquivoTest {

  @Test
  @DisplayName("Deve retornar os tamanhos mínimo e máximo da foto de perfil")
  void deveRetornarTamanhosDaFotoDePerfil() {
    final TamanhoArquivo tamanhoArquivo = TamanhoArquivo.FOTO_DE_PERFIL;

    assertEquals(500 * 1024L, tamanhoArquivo.getTamanhoMinimo());
    assertEquals(10 * 1024 * 1024L, tamanhoArquivo.getTamanhoMaximo());
  }

  @Test
  @DisplayName("Não deve retornar valores incorretos para os tamanhos")
  void naoDeveRetornarValoresIncorretosParaOsTamanhos() {
    final TamanhoArquivo tamanhoArquivo = TamanhoArquivo.FOTO_DE_PERFIL;

    assertNotEquals(100 * 1024L, tamanhoArquivo.getTamanhoMinimo());
    assertNotEquals(20 * 1024 * 1024L, tamanhoArquivo.getTamanhoMaximo());
  }

  @Test
  @DisplayName("O tamanho mínimo deve ser menor que o tamanho máximo")
  void tamanhoMinimoDeveSerMenorQueTamanhoMaximo() {
    final TamanhoArquivo tamanhoArquivo = TamanhoArquivo.FOTO_DE_PERFIL;

    assertTrue(tamanhoArquivo.getTamanhoMinimo() < tamanhoArquivo.getTamanhoMaximo());
  }
}
