package com.ufrn.dct.bsi.touchfy.application.usecases.permission;

import com.ufrn.dct.bsi.touchfy.domain.permission.repository.PermissionRepository;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ExcluirPermissionUseCase {

  private final PermissionRepository permissionRepository;

  public void execute(final Long id) {
    if (!permissionRepository.existePorId(id)) {
      throw new RecursoNaoEncontradoException("Permissão não encontrada para o ID: " + id);
    }
    permissionRepository.excluir(id);
  }
}
