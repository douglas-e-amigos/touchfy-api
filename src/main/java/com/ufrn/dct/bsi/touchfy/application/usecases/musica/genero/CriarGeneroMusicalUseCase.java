package com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarGeneroMusicalRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.GeneroMusicalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CriarGeneroMusicalUseCase {
  private final GeneroMusicalRepository repository;

  public void execute(final CriarGeneroMusicalRequest request) {
    if (request == null
        || request.nome() == ""
        || request.nome().length() > 100
        || request.nome().length() < 2) {
      throw new IllegalArgumentException("Os dados do gênero musical são inválidos.");
    }

    repository.salvar(request);
  }
}
