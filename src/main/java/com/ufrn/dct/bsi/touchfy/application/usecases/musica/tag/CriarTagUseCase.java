package com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarTagRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CriarTagUseCase {
    private final TagRepository repository;

    public void execute(final CriarTagRequest request) {
        if (request == null || request.nome() == ""
                || request.nome().length() > 100 || request.nome().length() < 2) {
            throw new IllegalArgumentException();
        }

        repository.salvar(request);
    }
}
