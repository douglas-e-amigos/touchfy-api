package com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.GeneroMusical;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.GeneroMusicalRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConsultarGenerosMusicaisUseCase {
  private final GeneroMusicalRepository repository;

  public List<GeneroMusical> execute() {
    return repository.consultar();
  }
}
