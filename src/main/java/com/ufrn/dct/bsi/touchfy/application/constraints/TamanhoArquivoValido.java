package com.ufrn.dct.bsi.touchfy.application.constraints;

import com.ufrn.dct.bsi.touchfy.application.enums.TamanhoArquivo;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ValidarTamanhoArquivo.class)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TamanhoArquivoValido {
    String message() default "O tamanho da imagem é inválido.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    TamanhoArquivo tamanhoArquivo();
}
