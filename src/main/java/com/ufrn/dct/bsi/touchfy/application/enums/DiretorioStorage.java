package com.ufrn.dct.bsi.touchfy.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DiretorioStorage {
  FOTOS_DE_PERFIL("usuarios/%s/perfil"),
  MUSICAS("musicas/%s");

  private final String diretorio;
}
