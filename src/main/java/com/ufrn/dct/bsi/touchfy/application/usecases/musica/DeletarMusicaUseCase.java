package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.DeletarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class DeletarMusicaUseCase {
    private final MusicaRepository repository;
    private final DeletarArquivoUseCase deletarArquivoUseCase;

    public void execute(final UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID é obrigatório.");
        }

        final var musica = repository.acharPeloId(id)
                .orElseThrow(() -> new RuntimeException("Música não encontrada."));

        if (musica.getCaminhoDoArquivo() != null) {
            deletarArquivoUseCase.execute(musica.getCaminhoDoArquivo());
        }

        repository.deletar(id);
    }
}