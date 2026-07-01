package com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarGeneroMusicalRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.GeneroMusicalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class AtualizarGeneroMusicalUseCase {
    private final GeneroMusicalRepository repository;

    public void execute(final UUID id, final AtualizarGeneroMusicalRequest request) {
        if (request == null || id == null || request.nome() == ""
                || request.nome().length() > 100 || request.nome().length() < 2) {
            throw new IllegalArgumentException("Os dados do gênero musical são inválidos.");
        }

        repository.atualizar(id, request);
    }
}