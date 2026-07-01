package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.GeneroMusicalEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeneroMusicalJpaRepository extends JpaRepository<GeneroMusicalEntity, UUID> {}
