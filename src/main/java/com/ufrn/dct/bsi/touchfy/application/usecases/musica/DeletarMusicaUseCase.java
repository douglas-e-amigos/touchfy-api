package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.DeletarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.AcessoNegadoException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.NaoAutenticadoException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeletarMusicaUseCase {
  private final MusicaRepository repository;
  private final DeletarArquivoUseCase deletarArquivoUseCase;
  private final AuditorAware<UUID> auditorAware;

  public void execute(final UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("ID é obrigatório.");
    }

    final var musica =
        repository
            .acharPeloId(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Música não encontrada."));
    validarPermissaoDeGerenciamento(musica);

    if (musica.getCaminhoDoArquivo() != null) {
      deletarArquivoUseCase.execute(musica.getCaminhoDoArquivo());
    }

    repository.deletar(id);
  }

  private void validarPermissaoDeGerenciamento(final Musica musica) {
    final UUID usuarioId =
        auditorAware
            .getCurrentAuditor()
            .orElseThrow(() -> new NaoAutenticadoException("Usuário não autenticado."));

    if (usuarioId.equals(musica.getCriadoPor()) || possuiRoleElevada()) {
      return;
    }

    throw new AcessoNegadoException("Usuário não tem permissão para deletar esta música.");
  }

  private boolean possuiRoleElevada() {
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || authentication.getAuthorities() == null) {
      return false;
    }

    return authentication.getAuthorities().stream()
        .anyMatch(
            authority ->
                "ROLE_ADMIN".equals(authority.getAuthority())
                    || "ROLE_MODERADOR".equals(authority.getAuthority()));
  }
}
