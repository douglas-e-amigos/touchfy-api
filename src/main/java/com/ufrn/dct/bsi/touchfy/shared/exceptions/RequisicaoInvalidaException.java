package com.ufrn.dct.bsi.touchfy.shared.exceptions;

import org.springframework.http.HttpStatus;

public class RequisicaoInvalidaException extends DomainException {
    public RequisicaoInvalidaException(final String mensagem) {
        super(mensagem, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
