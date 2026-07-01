package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.PlaylistMusicaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaylistMusicaJpaRepository extends JpaRepository<PlaylistMusicaEntity, UUID> {
  @Query("SELECT MAX(pm.ordem) FROM PlaylistMusicaEntity pm WHERE pm.playlist.id = :playlistId")
  Optional<Integer> maxOrdem(@Param("playlistId") UUID playlistId);

  Optional<PlaylistMusicaEntity> findByPlaylistIdAndMusicaId(UUID playlistId, UUID musicaId);
}
