package com.ufrn.dct.bsi.touchfy.application.usecases.role;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.RoleMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.role.RoleResponse;
import com.ufrn.dct.bsi.touchfy.domain.role.repository.RoleRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ListarRolesUseCase {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  public List<RoleResponse> execute() {
    return roleRepository.listarTodos().stream().map(roleMapper::toResponse).toList();
  }
}
