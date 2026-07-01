package com.ufrn.dct.bsi.touchfy.shared.exceptions;

import org.springframework.http.HttpStatus;

public class NaoAutenticadoException extends DomainException {
  public NaoAutenticadoException(final String mensagem) {
    super(mensagem, HttpStatus.UNAUTHORIZED);
  }
}
