package com.ufrn.dct.bsi.touchfy.application.usecases.role;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.RoleMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.role.RoleResponse;
import com.ufrn.dct.bsi.touchfy.domain.role.Role;
import com.ufrn.dct.bsi.touchfy.domain.role.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BuscarRolePorIdUseCase {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleResponse execute(final Long id) {
        final Role role = roleRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Perfil não encontrado para o ID: " + id));
        return roleMapper.toResponse(role);
    }
}
