package com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag;

import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.TagRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class DeletarTagUseCaseTest {

    @Test
    void deveDeletarTagQuandoIdForValido() {
        final TagRepository repository = mock(TagRepository.class);
        final DeletarTagUseCase useCase = new DeletarTagUseCase(repository);
        final UUID id = UUID.randomUUID();

        useCase.execute(id);

        verify(repository, times(1)).deletar(id);
    }

    @Test
    void deveLancarExcecaoQuandoIdForNulo() {
        final TagRepository repository = mock(TagRepository.class);
        final DeletarTagUseCase useCase = new DeletarTagUseCase(repository);

        final var exception = assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));

        assertEquals("ID é obrigatório.", exception.getMessage());
        verify(repository, never()).deletar(null);
    }
}