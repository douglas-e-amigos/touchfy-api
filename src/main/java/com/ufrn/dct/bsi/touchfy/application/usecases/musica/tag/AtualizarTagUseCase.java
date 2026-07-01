package com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarTagRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class AtualizarTagUseCase {
    private final TagRepository repository;

    public void execute(final UUID id, final AtualizarTagRequest request) {
        if (request == null || id == null || request.nome() == ""
                || request.nome().length() > 100 || request.nome().length() < 2) {
            throw new IllegalArgumentException("Os dados da tag são inválidos.");
        }

        repository.atualizar(id, request);
    }
}
