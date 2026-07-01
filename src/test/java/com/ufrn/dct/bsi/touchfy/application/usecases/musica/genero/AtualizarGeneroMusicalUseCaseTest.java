package com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarGeneroMusicalRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.GeneroMusicalRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AtualizarGeneroMusicalUseCaseTest {

  @Test
  void deveAtualizarGeneroMusicalQuandoRequestForValido() {
    final GeneroMusicalRepository repository = mock(GeneroMusicalRepository.class);
    final AtualizarGeneroMusicalUseCase useCase = new AtualizarGeneroMusicalUseCase(repository);
    final UUID id = UUID.randomUUID();
    final AtualizarGeneroMusicalRequest request =
        new AtualizarGeneroMusicalRequest("Rock progressivo");

    useCase.execute(id, request);

    verify(repository, times(1)).atualizar(id, request);
  }

  @Test
  void deveLancarExcecaoQuandoIdForNulo() {
    final GeneroMusicalRepository repository = mock(GeneroMusicalRepository.class);
    final AtualizarGeneroMusicalUseCase useCase = new AtualizarGeneroMusicalUseCase(repository);
    final AtualizarGeneroMusicalRequest request =
        new AtualizarGeneroMusicalRequest("Rock progressivo");

    final var exception =
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null, request));

    org.junit.jupiter.api.Assertions.assertNotNull(exception);
    verify(repository, never()).atualizar(null, request);
  }
}
