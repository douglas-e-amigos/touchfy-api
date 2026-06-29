package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.MusicaEntity;

public interface MusicaJpaRepository extends JpaRepository<MusicaEntity, UUID> {
    @Override
    List<MusicaEntity> findAll();

    List<MusicaEntity> findByCriadoPor(UUID criadoPor);

    List<MusicaEntity> findByCriadoPorIn(List<UUID> criadoPor);

    @Override
    Optional<MusicaEntity> findById(UUID id);
}
