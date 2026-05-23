package com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.Tag;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ConsultarTagsUseCase {
    private final TagRepository repository;

    public List<Tag> execute() {
        return repository.consultar();
    }
}
