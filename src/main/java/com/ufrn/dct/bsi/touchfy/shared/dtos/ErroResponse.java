package com.ufrn.dct.bsi.touchfy.shared.dtos;

import java.time.Instant;

public record ErroResponse(String mensagem, int statusCode, Instant timestamp, String traceId) {}
