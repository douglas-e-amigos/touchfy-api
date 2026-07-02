package com.ufrn.dct.bsi.touchfy.application.usecases.artista;

import com.ufrn.dct.bsi.touchfy.application.dtos.artista.ArtistaCompartilhamentoResponse;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GerarCompartilhamentoArtistaUseCase {
  private final UsuarioRepository usuarioRepository;
  private final String frontendBaseUrl;

  public GerarCompartilhamentoArtistaUseCase(
      final UsuarioRepository usuarioRepository,
      @Value("${app.frontend-base-url:http://localhost:3000}") final String frontendBaseUrl) {
    this.usuarioRepository = usuarioRepository;
    this.frontendBaseUrl = frontendBaseUrl;
  }

  public ArtistaCompartilhamentoResponse execute(final UUID artistaId) {
    final var usuario =
        usuarioRepository
            .acharPeloId(artistaId)
            .orElseThrow(() -> new RuntimeException("Artista não encontrado."));

    final String link = frontendBaseUrl + "/artistas/" + artistaId;

    return ArtistaCompartilhamentoResponse.builder()
        .link(link)
        .nomeArtista(usuario.getNome())
        .imagem(usuario.getCaminhoDaImagemDePerfil())
        .build();
  }
}
