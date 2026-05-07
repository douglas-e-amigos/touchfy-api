package com.ufrn.dct.bsi.touchfy.application.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DimensoesImagem {
    FOTO_DE_PERFIL(300, 720, 300, 720);

    private final int larguraMinima;
    private final int larguraMaxima;
    private final int alturaMinima;
    private final int alturaMaxima;
}
