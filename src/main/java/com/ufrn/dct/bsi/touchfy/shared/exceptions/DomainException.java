package com.ufrn.dct.bsi.touchfy.shared.exceptions;

import org.springframework.http.HttpStatus;

public abstract class DomainException extends RuntimeException {

    private final HttpStatus status;

    protected DomainException(final String mensagem, final HttpStatus status) {
        super(mensagem);
        this.status = status;
    }

    protected DomainException(final String mensagem, final HttpStatus status, final Throwable causa) {
        super(mensagem, causa);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
