package com.ufrn.dct.bsi.touchfy.application.usecases.musica.genero;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.GeneroMusical;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.GeneroMusicalRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConsultarGenerosMusicaisUseCaseTest {

    @Test
    void deveConsultarGenerosMusicais() {
        final GeneroMusicalRepository repository = mock(GeneroMusicalRepository.class);
        final ConsultarGenerosMusicaisUseCase useCase = new ConsultarGenerosMusicaisUseCase(repository);
        final List<GeneroMusical> generosMusicais = List.of(
                GeneroMusical.builder().id(UUID.randomUUID()).nome("Rock classico").build()
        );

        when(repository.consultar()).thenReturn(generosMusicais);

        final var response = useCase.execute();

        assertEquals(generosMusicais, response);
        verify(repository, times(1)).consultar();
    }
}