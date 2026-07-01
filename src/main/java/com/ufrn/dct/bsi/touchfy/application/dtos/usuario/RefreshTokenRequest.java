package com.ufrn.dct.bsi.touchfy.application.dtos.usuario;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank String refreshToken) {}
