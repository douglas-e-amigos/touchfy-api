package com.ufrn.dct.bsi.touchfy.application.usecases.artista;

import com.ufrn.dct.bsi.touchfy.application.dtos.artista.ArtistaPerfilResponse;
import com.ufrn.dct.bsi.touchfy.domain.artista.repositories.ArtistaRepository;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BuscarPerfilArtistaUseCase {
  private final UsuarioRepository usuarioRepository;
  private final ArtistaRepository artistaRepository;

  public ArtistaPerfilResponse execute(final UUID artistaId) {
    final var usuario =
        usuarioRepository
            .acharPeloId(artistaId)
            .orElseThrow(() -> new RuntimeException("Artista não encontrado."));

    final boolean ehArtista =
        usuario.getRoles().stream().anyMatch(r -> r.getName() == ERole.ARTISTA);

    if (!ehArtista) {
      throw new RuntimeException("Artista não encontrado.");
    }

    final long seguidores = artistaRepository.contarSeguidores(artistaId);

    return ArtistaPerfilResponse.builder()
        .id(usuario.getId())
        .nome(usuario.getNome())
        .nomeUsuario(usuario.getNomeUsuario())
        .descricao(usuario.getDescricao())
        .imagem(usuario.getCaminhoDaImagemDePerfil())
        .numeroDeOuvintes(seguidores)
        .posicaoRanking(null)
        .build();
  }
}
