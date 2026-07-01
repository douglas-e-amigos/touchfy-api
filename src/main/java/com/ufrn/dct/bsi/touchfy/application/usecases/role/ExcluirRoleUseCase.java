package com.ufrn.dct.bsi.touchfy.application.usecases.role;

import com.ufrn.dct.bsi.touchfy.domain.role.repository.RoleRepository;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ExcluirRoleUseCase {

    private final RoleRepository roleRepository;

    public void execute(final Long id) {
        if (!roleRepository.existePorId(id)) {
            throw new RecursoNaoEncontradoException("Perfil não encontrado para o ID: " + id);
        }
        roleRepository.excluir(id);
    }
}
