package com.ufrn.dct.bsi.touchfy.domain.permission.repository;

import com.ufrn.dct.bsi.touchfy.domain.permission.Permission;
import java.util.List;
import java.util.Optional;

public interface PermissionRepository {
    Permission salvar(Permission permission);
    Optional<Permission> buscarPorId(Long id);
    List<Permission> listarTodos();
    Permission atualizar(Permission permission);
    void excluir(Long id);
    boolean existePorId(Long id);
    boolean existePorNome(String nome);
}
