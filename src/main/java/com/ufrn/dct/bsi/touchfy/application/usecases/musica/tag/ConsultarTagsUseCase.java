package com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.Tag;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.TagRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConsultarTagsUseCase {
  private final TagRepository repository;

  public List<Tag> execute() {
    return repository.consultar();
  }
}
