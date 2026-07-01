package com.ufrn.dct.bsi.touchfy.shared.dtos;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record RecursoAtualizadoResponse(
    String mensagem, Boolean atualizado, LocalDate atualizadoEm) {}
