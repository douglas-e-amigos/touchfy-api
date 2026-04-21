package com.ufrn.dct.bsi.touchfy.shared.dtos;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record NovoRecursoResponse(
        String mensagem,
        Boolean criado,
        LocalDate criadoEm
) {
}
