package com.ufrn.dct.bsi.touchfy.application.usecases.artista;

import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PromoverUsuarioParaArtistaUseCase {
  private final UsuarioRepository usuarioRepository;

  public void execute(final UUID usuarioId) {
    final var usuario =
        usuarioRepository
            .acharPeloId(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

    final boolean jaEhArtista =
        usuario.getRoles().stream().anyMatch(r -> r.getName() == ERole.ARTISTA);

    if (jaEhArtista) {
      throw new RuntimeException("Conflito: usuário já possui o papel de artista.");
    }

    usuarioRepository.adicionarRole(usuarioId, ERole.ARTISTA);
  }
}
