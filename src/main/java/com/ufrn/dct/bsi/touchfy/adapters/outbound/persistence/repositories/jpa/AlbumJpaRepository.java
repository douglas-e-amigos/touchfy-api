package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlbumJpaRepository extends JpaRepository<AlbumEntity, UUID> {
}
