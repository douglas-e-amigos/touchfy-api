package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.ArtistaBloqueadoEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.ArtistaSeguidorEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.ArtistaBloqueadoJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.ArtistaSeguidorJpaRepository;
import com.ufrn.dct.bsi.touchfy.domain.artista.repositories.ArtistaRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ArtistaRepositoryImpl implements ArtistaRepository {
  private final ArtistaSeguidorJpaRepository seguidorJpaRepository;
  private final ArtistaBloqueadoJpaRepository bloqueadoJpaRepository;

  @Override
  public void seguir(final UUID artistaId, final UUID usuarioId) {
    seguidorJpaRepository.save(
        ArtistaSeguidorEntity.builder().artistaId(artistaId).usuarioId(usuarioId).build());
  }

  @Override
  public boolean existeSeguindo(final UUID artistaId, final UUID usuarioId) {
    return seguidorJpaRepository.existsByArtistaIdAndUsuarioId(artistaId, usuarioId);
  }

  @Override
  public long contarSeguidores(final UUID artistaId) {
    return seguidorJpaRepository.countByArtistaId(artistaId);
  }

  @Override
  public void bloquear(final UUID artistaId, final UUID usuarioId) {
    bloqueadoJpaRepository.save(
        ArtistaBloqueadoEntity.builder().artistaId(artistaId).usuarioId(usuarioId).build());
  }

  @Override
  public boolean existeBloqueado(final UUID artistaId, final UUID usuarioId) {
    return bloqueadoJpaRepository.existsByArtistaIdAndUsuarioId(artistaId, usuarioId);
  }
}
