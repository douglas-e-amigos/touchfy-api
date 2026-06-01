package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarMusicaRequest;
import com.ufrn.dct.bsi.touchfy.application.enums.DiretorioStorage;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.DeletarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.UploadArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class AtualizarMusicaUseCase {
    private final MusicaRepository repository;
    private final UploadArquivoUseCase uploadArquivoUseCase;
    private final DeletarArquivoUseCase deletarArquivoUseCase;

    public void execute(final UUID id, final AtualizarMusicaRequest request) {
        if (id == null || request == null) {
            throw new IllegalArgumentException("Os dados da música são obrigatórios.");
        }

        final var musica = repository.acharPeloId(id)
                .orElseThrow(() -> new RuntimeException("Música não encontrada."));

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
}