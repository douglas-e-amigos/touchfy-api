package com.ufrn.dct.bsi.touchfy.application.constraints;

import com.ufrn.dct.bsi.touchfy.application.enums.DimensoesImagem;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;

public class ValidarDimensoesImagem
    implements ConstraintValidator<DimensoesImagemValida, MultipartFile> {
  private DimensoesImagem dimensoes;

  @Override
  public void initialize(final DimensoesImagemValida constraintAnnotation) {
    this.dimensoes = constraintAnnotation.dimensoes();
  }

  @Override
  public boolean isValid(
      final MultipartFile multipartFile,
      final ConstraintValidatorContext constraintValidatorContext) {
    if (multipartFile == null || multipartFile.isEmpty()) {
      return true;
    }
    try {
      final BufferedImage imagem = ImageIO.read(multipartFile.getInputStream());
      if (imagem == null) {
        return false;
      }
      final int largura = imagem.getWidth();
      final int altura = imagem.getHeight();
      if (largura < this.dimensoes.getLarguraMinima()
          || largura > this.dimensoes.getLarguraMaxima()) {
        return false;
      }
      if (altura < this.dimensoes.getAlturaMinima() || altura > this.dimensoes.getAlturaMaxima()) {
        return false;
      }
    } catch (IOException e) {
      return false;
    }

    return true;
  }
}
