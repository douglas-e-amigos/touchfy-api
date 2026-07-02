package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.ArtistaSeguidorEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistaSeguidorJpaRepository extends JpaRepository<ArtistaSeguidorEntity, UUID> {
  boolean existsByArtistaIdAndUsuarioId(UUID artistaId, UUID usuarioId);

  long countByArtistaId(UUID artistaId);
}
