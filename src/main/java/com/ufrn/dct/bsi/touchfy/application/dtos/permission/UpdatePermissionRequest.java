package com.ufrn.dct.bsi.touchfy.application.dtos.permission;

import jakarta.validation.constraints.NotBlank;

public record UpdatePermissionRequest(
    @NotBlank(message = "O nome da permissão é obrigatório.") String name) {}
