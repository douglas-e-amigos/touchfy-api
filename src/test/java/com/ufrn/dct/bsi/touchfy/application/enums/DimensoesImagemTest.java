package com.ufrn.dct.bsi.touchfy.application.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DimensoesImagemTest {
    @Test
    @DisplayName("Deve retornar as dimensões válidas da foto de perfil")
    void deveRetornarDimensoesDaFotoDePerfil() {
        final DimensoesImagem dimensoesImagem = DimensoesImagem.FOTO_DE_PERFIL;
        assertEquals(300, dimensoesImagem.getLarguraMinima());
        assertEquals(720, dimensoesImagem.getLarguraMaxima());
        assertEquals(300, dimensoesImagem.getAlturaMinima());
        assertEquals(720, dimensoesImagem.getAlturaMaxima());
    }

    @Test
    @DisplayName("Não deve retornar dimensões incorretas")
    void naoDeveRetornarDimensoesIncorretas() {
        final DimensoesImagem dimensoesImagem = DimensoesImagem.FOTO_DE_PERFIL;
        assertNotEquals(100, dimensoesImagem.getLarguraMinima());
        assertNotEquals(1000, dimensoesImagem.getLarguraMaxima());
        assertNotEquals(100, dimensoesImagem.getAlturaMinima());
        assertNotEquals(1000, dimensoesImagem.getAlturaMaxima());
    }

    @Test
    @DisplayName("Dimensões mínimas devem ser menores que as máximas")
    void dimensoesMinimasDevemSerMenoresQueMaximas() {
        final DimensoesImagem dimensoesImagem = DimensoesImagem.FOTO_DE_PERFIL;
        assertTrue(dimensoesImagem.getLarguraMinima() < dimensoesImagem.getLarguraMaxima());
        assertTrue(dimensoesImagem.getAlturaMinima() < dimensoesImagem.getAlturaMaxima());
    }
}
