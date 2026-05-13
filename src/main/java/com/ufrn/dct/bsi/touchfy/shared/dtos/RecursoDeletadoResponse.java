package com.ufrn.dct.bsi.touchfy.shared.dtos;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record RecursoDeletadoResponse(
        String mensagem,
        Boolean deletado,
        LocalDate deletadoEm
) {
}