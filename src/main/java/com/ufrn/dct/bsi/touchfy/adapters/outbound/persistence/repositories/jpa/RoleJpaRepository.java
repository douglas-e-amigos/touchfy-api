package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.RoleEntity;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends JpaRepository<RoleEntity, Long> {
  Optional<RoleEntity> findByName(ERole name);

  boolean existsByName(ERole role);
}
