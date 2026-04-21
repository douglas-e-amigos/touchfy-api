package com.ufrn.dct.bsi.touchfy.shared.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImagemTest {
    @Test
    void deveCriarImagemComConstrutorCompleto() {
        Imagem imagem = new Imagem(
                "foto",
                "/tmp/foto.png",
                "png",
                1024.0,
                "uma foto"
        );

        assertEquals("foto", imagem.getNome());
        assertEquals("/tmp/foto.png", imagem.getCaminhoDoArquivo());
        assertEquals("png", imagem.getExtensao());
        assertEquals(1024.0, imagem.getTamanhoEmBytes());
        assertEquals("uma foto", imagem.getTextoAlternativo());
    }

    @Test
    void deveCriarImagemComBuilder() {
        Imagem imagem = Imagem.builder()
                .nome("foto")
                .caminhoDoArquivo("/tmp/foto.png")
                .extensao("png")
                .tamanhoEmBytes(2048.0)
                .textoAlternativo("descrição")
                .build();

        assertEquals("foto", imagem.getNome());
        assertEquals("/tmp/foto.png", imagem.getCaminhoDoArquivo());
        assertEquals("png", imagem.getExtensao());
        assertEquals(2048.0, imagem.getTamanhoEmBytes());
        assertEquals("descrição", imagem.getTextoAlternativo());
    }

    @Test
    void deveAlterarValoresComSetter() {
        Imagem imagem = new Imagem();

        imagem.setNome("img");
        imagem.setExtensao("jpg");

        assertEquals("img", imagem.getNome());
        assertEquals("jpg", imagem.getExtensao());
    }
}
