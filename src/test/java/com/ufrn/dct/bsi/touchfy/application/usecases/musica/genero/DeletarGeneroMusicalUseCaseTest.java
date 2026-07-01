package com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.GeneroMusicalRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class DeletarGeneroMusicalUseCaseTest {

  @Test
  void deveDeletarGeneroMusicalQuandoIdForValido() {
    final GeneroMusicalRepository repository = mock(GeneroMusicalRepository.class);
    final DeletarGeneroMusicalUseCase useCase = new DeletarGeneroMusicalUseCase(repository);
    final UUID id = UUID.randomUUID();

    useCase.execute(id);

    verify(repository, times(1)).deletar(id);
  }

  @Test
  void deveLancarExcecaoQuandoIdForNulo() {
    final GeneroMusicalRepository repository = mock(GeneroMusicalRepository.class);
    final DeletarGeneroMusicalUseCase useCase = new DeletarGeneroMusicalUseCase(repository);

    final var exception = assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));

    org.junit.jupiter.api.Assertions.assertNotNull(exception);
    verify(repository, never()).deletar(null);
  }
}
