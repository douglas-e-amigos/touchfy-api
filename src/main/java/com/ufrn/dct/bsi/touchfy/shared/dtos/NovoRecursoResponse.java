package com.ufrn.dct.bsi.touchfy.shared.dtos;

import java.time.LocalDate;
import lombok.Builder;

@Builder
public record NovoRecursoResponse(String mensagem, Boolean criado, LocalDate criadoEm) {}
