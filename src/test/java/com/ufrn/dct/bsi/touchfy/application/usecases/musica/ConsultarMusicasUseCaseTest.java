package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.MusicaMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.MusicaResponse;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConsultarMusicasUseCaseTest {

    @Test
    void deveConsultarMusicas() {
        final MusicaRepository repository = mock(MusicaRepository.class);
        final MusicaMapper musicaMapper = mock(MusicaMapper.class);
        final ConsultarMusicasUseCase useCase = new ConsultarMusicasUseCase(repository, musicaMapper);
        final Musica musica = Musica.builder()
                .id(UUID.randomUUID())
                .nome("Tempo Perdido")
                .caminhoDoArquivo("musicas/arquivo.mp3")
                .build();
        final MusicaResponse response = new MusicaResponse(
                musica.getId(),
                musica.getNome(),
                musica.getCaminhoDoArquivo(),
                null,
                List.of(),
                List.of()
        );

        when(repository.consultar()).thenReturn(List.of(musica));
        when(musicaMapper.toResponse(musica)).thenReturn(response);

        final List<MusicaResponse> resultado = useCase.execute();

        assertEquals(List.of(response), resultado);
        verify(repository, times(1)).consultar();
        verify(musicaMapper, times(1)).toResponse(musica);
    }
}