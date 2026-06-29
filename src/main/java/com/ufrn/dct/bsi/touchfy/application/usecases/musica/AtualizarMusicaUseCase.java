package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarMusicaRequest;
import com.ufrn.dct.bsi.touchfy.application.enums.DiretorioStorage;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.DeletarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.UploadArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class AtualizarMusicaUseCase {
    private final MusicaRepository repository;
    private final UploadArquivoUseCase uploadArquivoUseCase;
    private final DeletarArquivoUseCase deletarArquivoUseCase;
    private final AuditorAware<UUID> auditorAware;

    public void execute(final UUID id, final AtualizarMusicaRequest request) {
        if (id == null || request == null) {
            throw new IllegalArgumentException("Os dados da música são obrigatórios.");
        }

        final var musica = repository.acharPeloId(id)
                .orElseThrow(() -> new RuntimeException("Música não encontrada."));
        validarPermissaoDeGerenciamento(musica);

        String caminhoDoArquivo = null;
        if (request.arquivo() != null && !request.arquivo().isEmpty()) {
            if (musica.getCaminhoDoArquivo() != null) {
                deletarArquivoUseCase.execute(musica.getCaminhoDoArquivo());
            }

            final String diretorio = String.format(DiretorioStorage.MUSICAS.getDiretorio(), id);
            final var armazenamento = uploadArquivoUseCase.execute(request.arquivo(), diretorio);
            caminhoDoArquivo = armazenamento.caminhoDoArquivo();
        }

        repository.atualizar(id, request, caminhoDoArquivo);
    }

    private void validarPermissaoDeGerenciamento(final Musica musica) {
        final UUID usuarioId = auditorAware.getCurrentAuditor()
                .orElseThrow(() -> new RuntimeException("Usuário não autenticado."));

        if (usuarioId.equals(musica.getCriadoPor()) || possuiRoleElevada()) {
            return;
        }

        throw new AccessDeniedException("Usuário não tem permissão para alterar esta música.");
    }

    private boolean possuiRoleElevada() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }

        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority())
                        || "ROLE_MODERADOR".equals(authority.getAuthority()));
    }
}
