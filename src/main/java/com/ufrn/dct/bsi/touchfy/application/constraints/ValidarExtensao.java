package com.ufrn.dct.bsi.touchfy.application.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class ValidarExtensao implements ConstraintValidator<ExtensaoValida, MultipartFile> {
  private String[] extensoesValidas;

  @Override
  public void initialize(final ExtensaoValida constraintAnnotation) {
    this.extensoesValidas = constraintAnnotation.value();
  }

  @Override
  public boolean isValid(
      final MultipartFile multipartFile,
      final ConstraintValidatorContext constraintValidatorContext) {
    if (multipartFile == null || multipartFile.isEmpty()) {
      return true;
    }
    final String extensao = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
    return Arrays.asList(extensoesValidas).contains(extensao);
  }
}
