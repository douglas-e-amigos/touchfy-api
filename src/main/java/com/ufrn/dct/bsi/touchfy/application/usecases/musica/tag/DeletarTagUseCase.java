package com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag;

import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class DeletarTagUseCase {
    private final TagRepository repository;

    public void execute(final UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID é obrigatório.");
        }

        repository.deletar(id);
    }
}
