package com.ufrn.dct.bsi.touchfy.shared.exceptions;

import org.springframework.http.HttpStatus;

public class RecursoNaoEncontradoException extends DomainException {
    public RecursoNaoEncontradoException(final String mensagem) {
        super(mensagem, HttpStatus.NOT_FOUND);
    }
}
