package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumMusicaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumMusicaJpaRepository extends JpaRepository<AlbumMusicaEntity, UUID> {
  Optional<AlbumMusicaEntity> findByAlbumIdAndMusicaId(UUID albumId, UUID musicaId);
}
