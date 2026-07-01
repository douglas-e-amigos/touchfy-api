package com.ufrn.dct.bsi.touchfy.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TamanhoArquivo {
  FOTO_DE_PERFIL(500 * 1024L, 10 * 1024 * 1024L), // 500KB até 10MB
  MUSICA(500 * 1024L, 20 * 1024 * 1024L); // 500KB até 20MB

  private final long tamanhoMinimo;
  private final long tamanhoMaximo;
}
