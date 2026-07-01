package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.PlaylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PlaylistJpaRepository extends JpaRepository<PlaylistEntity, UUID> {
    @Query("SELECT p FROM PlaylistEntity p WHERE p.donoId = :usuarioId")
    List<PlaylistEntity> listarDoUsuario(@Param("usuarioId") UUID usuarioId);
}
