package com.ufrn.dct.bsi.touchfy.application.constraints;

import com.ufrn.dct.bsi.touchfy.application.enums.TamanhoArquivo;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidarTamanhoArquivoTest {

    private ValidarTamanhoArquivo validator;

    @BeforeEach
    void setUp() {
        validator = new ValidarTamanhoArquivo();

        final TamanhoArquivoValido annotation =
                mock(TamanhoArquivoValido.class);

        when(annotation.tamanhoArquivo())
                .thenReturn(TamanhoArquivo.FOTO_DE_PERFIL);

        validator.initialize(annotation);
    }

    @Test
    @DisplayName("Deve retornar verdadeiro para arquivo com tamanho válido")
    void deveRetornarVerdadeiroParaArquivoComTamanhoValido() {
        final byte[] conteudo = new byte[1024 * 1024];

        final MultipartFile arquivo =
                new MockMultipartFile(
                        "foto",
                        "foto.png",
                        "image/png",
                        conteudo
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
    @DisplayName("Deve retornar falso para arquivo menor que o tamanho mínimo")
    void deveRetornarFalsoParaArquivoMenorQueTamanhoMinimo() {
        final byte[] conteudo = new byte[100 * 1024];

        final MultipartFile arquivo =
                new MockMultipartFile(
                        "foto",
                        "foto.png",
                        "image/png",
                        conteudo
                );

        final boolean resultado =
                validator.isValid(
                        arquivo,
                        mock(ConstraintValidatorContext.class)
                );

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve retornar falso para arquivo maior que o tamanho máximo")
    void deveRetornarFalsoParaArquivoMaiorQueTamanhoMaximo() {
        final byte[] conteudo =
                new byte[11 * 1024 * 1024];

        final MultipartFile arquivo =
                new MockMultipartFile(
                        "foto",
                        "foto.png",
                        "image/png",
                        conteudo
                );

        final boolean resultado =
                validator.isValid(
                        arquivo,
                        mock(ConstraintValidatorContext.class)
                );

        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve aceitar arquivo exatamente no tamanho mínimo")
    void deveAceitarArquivoExatamenteNoTamanhoMinimo() {
        final byte[] conteudo =
                new byte[500 * 1024];

        final MultipartFile arquivo =
                new MockMultipartFile(
                        "foto",
                        "foto.png",
                        "image/png",
                        conteudo
                );

        final boolean resultado =
                validator.isValid(
                        arquivo,
                        mock(ConstraintValidatorContext.class)
                );

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve aceitar arquivo exatamente no tamanho máximo")
    void deveAceitarArquivoExatamenteNoTamanhoMaximo() {
        final byte[] conteudo =
                new byte[10 * 1024 * 1024];

        final MultipartFile arquivo =
                new MockMultipartFile(
                        "foto",
                        "foto.png",
                        "image/png",
                        conteudo
                );

        final boolean resultado =
                validator.isValid(
                        arquivo,
                        mock(ConstraintValidatorContext.class)
                );

        assertTrue(resultado);
    }

    @Test
    @DisplayName("Deve lançar exceção quando tamanho mínimo for maior que tamanho máximo")
    void deveLancarExcecaoQuandoTamanhoMinimoForMaiorQueTamanhoMaximo() {
        final ValidarTamanhoArquivo validator =
                new ValidarTamanhoArquivo();

        final TamanhoArquivo tamanhoArquivo =
                mock(TamanhoArquivo.class);

        when(tamanhoArquivo.getTamanhoMinimo())
                .thenReturn(1000L);

        when(tamanhoArquivo.getTamanhoMaximo())
                .thenReturn(100L);

        final TamanhoArquivoValido annotation =
                mock(TamanhoArquivoValido.class);

        when(annotation.tamanhoArquivo())
                .thenReturn(tamanhoArquivo);

        validator.initialize(annotation);

        final MultipartFile arquivo =
                new MockMultipartFile(
                        "foto",
                        "foto.png",
                        "image/png",
                        new byte[500]
                );

        final RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> validator.isValid(
                                arquivo,
                                mock(ConstraintValidatorContext.class)
                        )
                );

        assertEquals(
                "O tamanho mínimo do arquivo deve ser menor ou igual que o tamanho máximo.",
                exception.getMessage()
        );
    }
}