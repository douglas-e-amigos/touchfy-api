package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumSalvoEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumSalvoJpaRepository extends JpaRepository<AlbumSalvoEntity, UUID> {
  Optional<AlbumSalvoEntity> findByAlbumIdAndUsuarioId(UUID albumId, UUID usuarioId);

  List<AlbumSalvoEntity> findByUsuarioId(UUID usuarioId);

  boolean existsByAlbumIdAndUsuarioId(UUID albumId, UUID usuarioId);
}
