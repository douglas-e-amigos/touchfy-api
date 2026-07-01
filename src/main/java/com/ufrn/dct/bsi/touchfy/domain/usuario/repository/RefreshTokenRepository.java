package com.ufrn.dct.bsi.touchfy.domain.usuario.repository;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.RefreshTokenEntity;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import java.util.Optional;

public interface RefreshTokenRepository {
  Optional<RefreshTokenEntity> acharPeloToken(final String token);

  void salvar(final Usuario usuario, final String token);

  void revogar(final String token);
}
