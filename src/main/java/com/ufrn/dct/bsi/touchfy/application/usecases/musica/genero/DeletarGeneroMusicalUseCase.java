package com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero;

import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.GeneroMusicalRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DeletarGeneroMusicalUseCase {
  private final GeneroMusicalRepository repository;

  public void execute(final UUID id) {
    if (id == null) {
      throw new IllegalArgumentException("ID é obrigatório.");
    }

    repository.deletar(id);
  }
}
