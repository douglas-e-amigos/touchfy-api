package com.ufrn.dct.bsi.touchfy.application.constraints;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidarExtensaoTest {

    private ValidarExtensao validator;

    @BeforeEach
    void setUp() {
        validator = new ValidarExtensao();

        final ExtensaoValida annotation =
                mock(ExtensaoValida.class);

        when(annotation.value())
                .thenReturn(new String[]{"jpg", "jpeg", "png"});

        validator.initialize(annotation);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro para extensão válida jpg")
    void deveRetornarVerdadeiroParaExtensaoJpg() {
        final MultipartFile arquivo =
                new MockMultipartFile(
                        "foto",
                        "foto.jpg",
                        "image/jpeg",
                        "conteudo".getBytes()
                );

        final boolean resultado =
                validator.isValid(
                        arquivo,
                        mock(ConstraintValidatorContext.class)
                );

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro para extensão válida jpeg")
    void deveRetornarVerdadeiroParaExtensaoJpeg() {
        final MultipartFile arquivo =
                new MockMultipartFile(
                        "foto",
                        "foto.jpeg",
                        "image/jpeg",
                        "conteudo".getBytes()
                );

        final boolean resultado =
                validator.isValid(
                        arquivo,
                        mock(ConstraintValidatorContext.class)
                );

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro para extensão válida png")
    void deveRetornarVerdadeiroParaExtensaoPng() {
        final MultipartFile arquivo =
                new MockMultipartFile(
                        "foto",
                        "foto.png",
                        "image/png",
                        "conteudo".getBytes()
                );

        final boolean resultado =
                validator.isValid(
                        arquivo,
                        mock(ConstraintValidatorContext.class)
                );

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro quando arquivo for nulo")
    void deveRetornarVerdadeiroQuandoArquivoForNulo() {
        final boolean resultado =
                validator.isValid(
                        null,
                        mock(ConstraintValidatorContext.class)
                );

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro quando arquivo estiver vazio")
    void deveRetornarVerdadeiroQuandoArquivoEstiverVazio() {
        final MultipartFile arquivo =
                new MockMultipartFile(
                        "foto",
                        new byte[0]
                );

        final boolean resultado =
                validator.isValid(
                        arquivo,
                        mock(ConstraintValidatorContext.class)
                );

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve retornar falso para extensão inválida")
    void deveRetornarFalsoParaExtensaoInvalida() {
        final MultipartFile arquivo =
                new MockMultipartFile(
                        "foto",
                        "foto.gif",
                        "image/gif",
                        "conteudo".getBytes()
                );

        final boolean resultado =
                validator.isValid(
                        arquivo,
                        mock(ConstraintValidatorContext.class)
                );

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve retornar falso para arquivo sem extensão")
    void deveRetornarFalsoParaArquivoSemExtensao() {
        final MultipartFile arquivo =
                new MockMultipartFile(
                        "foto",
                        "foto",
                        "image/jpeg",
                        "conteudo".getBytes()
                );

        final boolean resultado =
                validator.isValid(
                        arquivo,
                        mock(ConstraintValidatorContext.class)
                );

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve retornar falso para extensão maiúscula")
    void deveRetornarFalsoParaExtensaoMaiuscula() {
        final MultipartFile arquivo =
                new MockMultipartFile(
                        "foto",
                        "foto.JPG",
                        "image/jpeg",
                        "conteudo".getBytes()
                );

        final boolean resultado =
                validator.isValid(
                        arquivo,
                        mock(ConstraintValidatorContext.class)
                );

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Não deve aceitar extensão não configurada")
    void naoDeveAceitarExtensaoNaoConfigurada() {
        final MultipartFile arquivo =
                new MockMultipartFile(
                        "foto",
                        "foto.bmp",
                        "image/bmp",
                        "conteudo".getBytes()
                );

        final boolean resultado =
                validator.isValid(
                        arquivo,
                        mock(ConstraintValidatorContext.class)
                );

        assertFalse(resultado);
    }
}