package com.ufrn.dct.bsi.touchfy.shared.exceptions;

import org.springframework.http.HttpStatus;

public class ConflitoDeNegocioException extends DomainException {
    public ConflitoDeNegocioException(final String mensagem) {
        super(mensagem, HttpStatus.CONFLICT);
    }
}
