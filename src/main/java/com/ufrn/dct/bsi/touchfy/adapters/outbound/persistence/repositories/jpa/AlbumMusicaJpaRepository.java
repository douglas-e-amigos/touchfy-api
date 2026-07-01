package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumMusicaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AlbumMusicaJpaRepository extends JpaRepository<AlbumMusicaEntity, UUID> {
    Optional<AlbumMusicaEntity> findByAlbumIdAndMusicaId(UUID albumId, UUID musicaId);
}
