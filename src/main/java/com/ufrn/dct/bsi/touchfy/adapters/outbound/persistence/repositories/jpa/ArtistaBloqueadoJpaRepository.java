package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.ArtistaBloqueadoEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistaBloqueadoJpaRepository extends JpaRepository<ArtistaBloqueadoEntity, UUID> {
  boolean existsByArtistaIdAndUsuarioId(UUID artistaId, UUID usuarioId);
}
