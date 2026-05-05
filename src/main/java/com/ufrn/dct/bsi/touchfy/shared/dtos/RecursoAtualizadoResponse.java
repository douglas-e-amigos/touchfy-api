package com.ufrn.dct.bsi.touchfy.shared.dtos;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record RecursoAtualizadoResponse(
        String mensagem,
        Boolean atualizado,
        LocalDate atualizadoEm
) {
}
