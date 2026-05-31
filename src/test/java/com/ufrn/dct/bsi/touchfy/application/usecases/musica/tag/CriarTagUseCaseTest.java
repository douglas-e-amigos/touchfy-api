package com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarTagRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.TagRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class CriarTagUseCaseTest {

    @Test
    void deveCriarTagQuandoRequestForValido() {
        final TagRepository repository = mock(TagRepository.class);
        final CriarTagUseCase useCase = new CriarTagUseCase(repository);
        final CriarTagRequest request = new CriarTagRequest("rock");

        useCase.execute(request);

        verify(repository, times(1)).salvar(request);
    }

    @Test
    void deveLancarExcecaoQuandoRequestForInvalido() {
        final TagRepository repository = mock(TagRepository.class);
        final CriarTagUseCase useCase = new CriarTagUseCase(repository);
        final CriarTagRequest request = new CriarTagRequest("r");

        final var exception = assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));

        assertNotNull(exception);
        verify(repository, never()).salvar(request);
    }
}