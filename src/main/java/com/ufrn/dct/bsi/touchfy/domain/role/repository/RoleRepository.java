package com.ufrn.dct.bsi.touchfy.domain.role.repository;

import com.ufrn.dct.bsi.touchfy.domain.role.Role;
import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    Role salvar(Role role);
    Optional<Role> buscarPorId(Long id);
    List<Role> listarTodos();
    Role atualizar(Role role);
    void excluir(Long id);
    boolean existePorId(Long id);
}
