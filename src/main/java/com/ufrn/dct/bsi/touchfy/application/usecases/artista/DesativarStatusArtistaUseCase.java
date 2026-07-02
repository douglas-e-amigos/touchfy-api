package com.ufrn.dct.bsi.touchfy.application.usecases.artista;

import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DesativarStatusArtistaUseCase {
  private final UsuarioRepository usuarioRepository;

  public void execute(final UUID artistaId, final UUID usuarioLogadoId) {
    if (!artistaId.equals(usuarioLogadoId)) {
      throw new RuntimeException("Usuário não autorizado a desativar este status de artista.");
    }

    final var usuario =
        usuarioRepository
            .acharPeloId(artistaId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

    final boolean ehArtista =
        usuario.getRoles().stream().anyMatch(r -> r.getName() == ERole.ARTISTA);

    if (!ehArtista) {
      throw new RuntimeException("Conflito: usuário não possui status de artista ativo.");
    }

    usuarioRepository.removerRole(artistaId, ERole.ARTISTA);
  }
}
