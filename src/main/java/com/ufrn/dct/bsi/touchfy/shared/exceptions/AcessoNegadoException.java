package com.ufrn.dct.bsi.touchfy.shared.exceptions;

import org.springframework.http.HttpStatus;

public class AcessoNegadoException extends DomainException {
  public AcessoNegadoException(final String mensagem) {
    super(mensagem, HttpStatus.FORBIDDEN);
  }
}
