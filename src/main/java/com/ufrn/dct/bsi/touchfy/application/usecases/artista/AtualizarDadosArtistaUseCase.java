package com.ufrn.dct.bsi.touchfy.application.usecases.artista;

import com.ufrn.dct.bsi.touchfy.application.dtos.artista.AtualizarDadosArtistaRequest;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AtualizarDadosArtistaUseCase {
  private final UsuarioRepository usuarioRepository;

  public void execute(
      final UUID artistaId,
      final AtualizarDadosArtistaRequest request,
      final UUID usuarioLogadoId) {
    if (!artistaId.equals(usuarioLogadoId)) {
      throw new RuntimeException("Usuário não autorizado a editar este perfil de artista.");
    }

    final var usuario =
        usuarioRepository
            .acharPeloId(artistaId)
            .orElseThrow(() -> new RuntimeException("Artista não encontrado."));

    final boolean ehArtista =
        usuario.getRoles().stream().anyMatch(r -> r.getName() == ERole.ARTISTA);

    if (!ehArtista) {
      throw new RuntimeException("Usuário não autorizado a editar este perfil de artista.");
    }

    if (request.nome() == null && request.descricao() == null) {
      throw new IllegalArgumentException("Informe ao menos um campo para atualizar.");
    }

    usuarioRepository.atualizarDadosArtista(artistaId, request);
  }
}
