package com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarGeneroMusicalRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.GeneroMusicalRepository;
import org.junit.jupiter.api.Test;

class CriarGeneroMusicalUseCaseTest {

  @Test
  void deveCriarGeneroMusicalQuandoRequestForValido() {
    final GeneroMusicalRepository repository = mock(GeneroMusicalRepository.class);
    final CriarGeneroMusicalUseCase useCase = new CriarGeneroMusicalUseCase(repository);
    final CriarGeneroMusicalRequest request = new CriarGeneroMusicalRequest("Rock classico");

    useCase.execute(request);

    verify(repository, times(1)).salvar(request);
  }

  @Test
  void deveLancarExcecaoQuandoRequestForInvalido() {
    final GeneroMusicalRepository repository = mock(GeneroMusicalRepository.class);
    final CriarGeneroMusicalUseCase useCase = new CriarGeneroMusicalUseCase(repository);
    final CriarGeneroMusicalRequest request = new CriarGeneroMusicalRequest("t");

    final var exception =
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));

    org.junit.jupiter.api.Assertions.assertNotNull(exception);
    verify(repository, never()).salvar(request);
  }
}
