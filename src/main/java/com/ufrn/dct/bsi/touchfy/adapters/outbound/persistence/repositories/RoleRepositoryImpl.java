package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.RoleMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.RoleJpaRepository;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.role.Role;
import com.ufrn.dct.bsi.touchfy.domain.role.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository jpaRepository;
    private final RoleMapper mapper;

    @Override
    public Role salvar(final Role role) {
        final var entity = mapper.toEntity(role);
        final var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Role> buscarPorId(final Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Role> listarTodos() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Role atualizar(final Role role) {
        final var entity = mapper.toEntity(role);
        final var updatedEntity = jpaRepository.save(entity);
        return mapper.toDomain(updatedEntity);
    }

    @Override
    public void excluir(final Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorId(final Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existePorNome(final ERole role) { return jpaRepository.existsByName(role); }
}
