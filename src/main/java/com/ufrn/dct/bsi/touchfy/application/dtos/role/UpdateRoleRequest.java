package com.ufrn.dct.bsi.touchfy.application.dtos.role;

import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record UpdateRoleRequest(
    @NotNull(message = "O nome do perfil é obrigatório.") ERole name, List<Long> permissionIds) {}
