package com.ufrn.dct.bsi.touchfy.application.dtos.role;

import com.ufrn.dct.bsi.touchfy.application.dtos.permission.PermissionResponse;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import java.util.List;

public record RoleResponse(Long id, ERole name, List<PermissionResponse> permissions) {}
