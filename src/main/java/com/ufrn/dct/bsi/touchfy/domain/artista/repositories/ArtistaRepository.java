package com.ufrn.dct.bsi.touchfy.domain.artista.repositories;

import java.util.UUID;

public interface ArtistaRepository {
  void seguir(UUID artistaId, UUID usuarioId);

  boolean existeSeguindo(UUID artistaId, UUID usuarioId);

  long contarSeguidores(UUID artistaId);

  void bloquear(UUID artistaId, UUID usuarioId);

  boolean existeBloqueado(UUID artistaId, UUID usuarioId);
}
