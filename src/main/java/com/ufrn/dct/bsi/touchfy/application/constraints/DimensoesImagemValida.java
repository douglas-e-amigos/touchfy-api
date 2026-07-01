package com.ufrn.dct.bsi.touchfy.application.constraints;

import com.ufrn.dct.bsi.touchfy.application.enums.DimensoesImagem;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = ValidarDimensoesImagem.class)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DimensoesImagemValida {
  String message() default "As dimensões da imagem estão inválidas.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  DimensoesImagem dimensoes();
}
