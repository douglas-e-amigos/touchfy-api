package com.ufrn.dct.bsi.touchfy.application.constraints;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ufrn.dct.bsi.touchfy.application.enums.DimensoesImagem;
import jakarta.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class ValidarDimensoesImagemTest {

  private ValidarDimensoesImagem validator;

  @BeforeEach
  void setUp() {
    validator = new ValidarDimensoesImagem();

    final DimensoesImagemValida annotation = mock(DimensoesImagemValida.class);

    when(annotation.dimensoes()).thenReturn(DimensoesImagem.FOTO_DE_PERFIL);

    validator.initialize(annotation);
  }

  @Test
  @DisplayName("Deve retornar verdadeiro para imagem válida")
  void deveRetornarVerdadeiroParaImagemValida() throws Exception {
    final MultipartFile arquivo = criarImagem(500, 500);
    final boolean resultado = validator.isValid(arquivo, mock(ConstraintValidatorContext.class));
    assertTrue(resultado);
  }

  @Test
  @DisplayName("Deve retornar verdadeiro quando arquivo for nulo")
  void deveRetornarVerdadeiroQuandoArquivoForNulo() {
    final boolean resultado = validator.isValid(null, mock(ConstraintValidatorContext.class));
    assertTrue(resultado);
  }

  @Test
  @DisplayName("Deve retornar verdadeiro quando arquivo estiver vazio")
  void deveRetornarVerdadeiroQuandoArquivoEstiverVazio() {
    final MultipartFile arquivo = new MockMultipartFile("foto", new byte[0]);
    final boolean resultado = validator.isValid(arquivo, mock(ConstraintValidatorContext.class));
    assertTrue(resultado);
  }

  @Test
  @DisplayName("Deve retornar falso para largura menor que a mínima")
  void deveRetornarFalsoParaLarguraMenorQueMinima() throws Exception {
    final MultipartFile arquivo = criarImagem(200, 500);
    final boolean resultado = validator.isValid(arquivo, mock(ConstraintValidatorContext.class));
    assertFalse(resultado);
  }

  @Test
  @DisplayName("Deve retornar falso para largura maior que a máxima")
  void deveRetornarFalsoParaLarguraMaiorQueMaxima() throws Exception {
    final MultipartFile arquivo = criarImagem(800, 500);
    final boolean resultado = validator.isValid(arquivo, mock(ConstraintValidatorContext.class));
    assertFalse(resultado);
  }

  @Test
  @DisplayName("Deve retornar falso para altura menor que a mínima")
  void deveRetornarFalsoParaAlturaMenorQueMinima() throws Exception {
    final MultipartFile arquivo = criarImagem(500, 200);
    final boolean resultado = validator.isValid(arquivo, mock(ConstraintValidatorContext.class));
    assertFalse(resultado);
  }

  @Test
  @DisplayName("Deve retornar falso para altura maior que a máxima")
  void deveRetornarFalsoParaAlturaMaiorQueMaxima() throws Exception {
    final MultipartFile arquivo = criarImagem(500, 800);
    final boolean resultado = validator.isValid(arquivo, mock(ConstraintValidatorContext.class));
    assertFalse(resultado);
  }

  @Test
  @DisplayName("Deve retornar falso quando arquivo não for imagem")
  void deveRetornarFalsoQuandoArquivoNaoForImagem() {
    final MultipartFile arquivo =
        new MockMultipartFile("foto", "teste.txt", "text/plain", "conteudo".getBytes());
    final boolean resultado = validator.isValid(arquivo, mock(ConstraintValidatorContext.class));
    assertFalse(resultado);
  }

  @Test
  @DisplayName("Deve retornar falso quando ocorrer IOException")
  void deveRetornarFalsoQuandoOcorrerIOException() throws Exception {
    final MultipartFile arquivo = mock(MultipartFile.class);
    when(arquivo.isEmpty()).thenReturn(false);
    when(arquivo.getInputStream()).thenThrow(new IOException());
    final boolean resultado = validator.isValid(arquivo, mock(ConstraintValidatorContext.class));
    assertFalse(resultado);
  }

  private MultipartFile criarImagem(final int largura, final int altura) throws Exception {
    final BufferedImage imagem = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    ImageIO.write(imagem, "png", outputStream);
    return new MockMultipartFile("foto", "foto.png", "image/png", outputStream.toByteArray());
  }
}
