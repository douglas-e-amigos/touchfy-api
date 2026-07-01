package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, UUID> {
  Optional<UsuarioEntity> findByNomeUsuario(String nome);

  List<UsuarioEntity> findByNomeContainingIgnoreCaseOrNomeUsuarioContainingIgnoreCase(
      String nome, String nomeUsuario);
}
