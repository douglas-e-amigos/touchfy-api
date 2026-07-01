package com.ufrn.dct.bsi.touchfy.application.constraints;

import com.ufrn.dct.bsi.touchfy.application.enums.TamanhoArquivo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ValidarTamanhoArquivo
    implements ConstraintValidator<TamanhoArquivoValido, MultipartFile> {
  private TamanhoArquivo tamanhoArquivo;

  @Override
  public void initialize(final TamanhoArquivoValido constraintAnnotation) {
    this.tamanhoArquivo = constraintAnnotation.tamanhoArquivo();
  }

  @Override
  public boolean isValid(
      final MultipartFile multipartFile,
      final ConstraintValidatorContext constraintValidatorContext) {
    if (multipartFile == null || multipartFile.isEmpty()) {
      return true;
    }
    if (this.tamanhoArquivo.getTamanhoMinimo() > this.tamanhoArquivo.getTamanhoMaximo()) {
      final String msg =
          "O tamanho mínimo do arquivo deve ser menor ou igual" + " que o tamanho máximo.";
      throw new IllegalArgumentException(msg);
    }
    final long tamanhoArquivo = multipartFile.getSize();

    return tamanhoArquivo >= this.tamanhoArquivo.getTamanhoMinimo()
        && tamanhoArquivo <= this.tamanhoArquivo.getTamanhoMaximo();
  }
}
