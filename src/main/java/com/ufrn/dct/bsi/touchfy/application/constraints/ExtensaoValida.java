package com.ufrn.dct.bsi.touchfy.application.constraints;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ValidarExtensao.class)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtensaoValida {
    String[] value() default {};
    String message() default "A extensão da imagem não é permitida.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
