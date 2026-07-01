package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AlbumEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlbumJpaRepository extends JpaRepository<AlbumEntity, UUID> {}
