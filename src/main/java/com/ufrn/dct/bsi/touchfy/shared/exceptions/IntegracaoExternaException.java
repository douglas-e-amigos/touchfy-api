package com.ufrn.dct.bsi.touchfy.shared.exceptions;

import org.springframework.http.HttpStatus;

public class IntegracaoExternaException extends DomainException {
  public IntegracaoExternaException(final String mensagem, final Throwable causa) {
    super(mensagem, HttpStatus.BAD_GATEWAY, causa);
  }
}
