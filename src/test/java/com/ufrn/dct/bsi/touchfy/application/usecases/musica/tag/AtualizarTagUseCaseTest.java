package com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarTagRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.TagRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AtualizarTagUseCaseTest {

  @Test
  void deveAtualizarTagQuandoRequestForValido() {
    final TagRepository repository = mock(TagRepository.class);
    final AtualizarTagUseCase useCase = new AtualizarTagUseCase(repository);
    final UUID id = UUID.randomUUID();
    final AtualizarTagRequest request = new AtualizarTagRequest("rock");

    useCase.execute(id, request);

    verify(repository, times(1)).atualizar(id, request);
  }

  @Test
  void deveLancarExcecaoQuandoIdForNulo() {
    final TagRepository repository = mock(TagRepository.class);
    final AtualizarTagUseCase useCase = new AtualizarTagUseCase(repository);
    final AtualizarTagRequest request = new AtualizarTagRequest("rock");

    final var exception =
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null, request));

    assertNotNull(exception);
    verify(repository, never()).atualizar(null, request);
  }
}
