package com.ufrn.dct.bsi.touchfy.application.usecases.artista;

import com.ufrn.dct.bsi.touchfy.domain.artista.repositories.ArtistaRepository;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BloquearArtistaUseCase {
  private final ArtistaRepository artistaRepository;
  private final UsuarioRepository usuarioRepository;

  public void execute(final UUID artistaId, final UUID usuarioId) {
    final var artista =
        usuarioRepository
            .acharPeloId(artistaId)
            .orElseThrow(() -> new RuntimeException("Artista não encontrado."));

    final boolean ehArtista =
        artista.getRoles().stream().anyMatch(r -> r.getName() == ERole.ARTISTA);

    if (!ehArtista) {
      throw new RuntimeException("Artista não encontrado.");
    }

    if (artistaRepository.existeBloqueado(artistaId, usuarioId)) {
      throw new RuntimeException("Conflito: artista já está bloqueado.");
    }

    artistaRepository.bloquear(artistaId, usuarioId);
  }
}
