package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.PermissionEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionJpaRepository extends JpaRepository<PermissionEntity, Long> {
  Optional<PermissionEntity> findByName(String name);

  Boolean existsByName(String name);
}
