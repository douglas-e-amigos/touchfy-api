package com.ufrn.dct.bsi.touchfy.application.dtos.usuario;

import lombok.Builder;

@Builder
public record TokenResponse(String accessToken, String refreshToken) {}
