package com.ufrn.dct.bsi.touchfy.application.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DiretorioStorageTest {

  @Test
  @DisplayName("Deve retornar o diretório de fotos de perfil")
  void deveRetornarDiretorioDeFotosDePerfil() {
    final DiretorioStorage diretorioStorage = DiretorioStorage.FOTOS_DE_PERFIL;
    assertEquals("usuarios/%s/perfil", diretorioStorage.getDiretorio());
  }

  @Test
  @DisplayName("Não deve retornar diretório incorreto")
  void naoDeveRetornarDiretorioIncorreto() {
    final DiretorioStorage diretorioStorage = DiretorioStorage.FOTOS_DE_PERFIL;
    assertNotEquals("usuarios/perfil", diretorioStorage.getDiretorio());
  }

  @Test
  @DisplayName("Diretório não deve ser nulo ou vazio")
  void diretorioNaoDeveSerNuloOuVazio() {
    final DiretorioStorage diretorioStorage = DiretorioStorage.FOTOS_DE_PERFIL;
    assertNotNull(diretorioStorage.getDiretorio());
    assertFalse(diretorioStorage.getDiretorio().isBlank());
  }
}
