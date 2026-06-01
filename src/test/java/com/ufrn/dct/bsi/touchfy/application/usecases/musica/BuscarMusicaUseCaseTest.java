package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.MusicaMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.MusicaResponse;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BuscarMusicaUseCaseTest {

    @Test
    void deveBuscarMusicaPorId() {
        final MusicaRepository repository = mock(MusicaRepository.class);
        final MusicaMapper musicaMapper = mock(MusicaMapper.class);
        final BuscarMusicaUseCase useCase = new BuscarMusicaUseCase(repository, musicaMapper);
        final UUID id = UUID.randomUUID();
        final Musica musica = Musica.builder()
                .id(id)
                .nome("Tempo Perdido")
                .caminhoDoArquivo("musicas/arquivo.mp3")
                .tags(List.of())
                .generosMusicais(List.of())
                .build();
        final MusicaResponse response = new MusicaResponse(id, "Tempo Perdido", "musicas/arquivo.mp3", null, List.of(), List.of());

        when(repository.acharPeloId(id)).thenReturn(Optional.of(musica));
        when(musicaMapper.toResponse(musica)).thenReturn(response);

        final MusicaResponse resultado = useCase.execute(id);

        assertEquals(response, resultado);
        verify(repository, times(1)).acharPeloId(id);
        verify(musicaMapper, times(1)).toResponse(musica);
    }

    @Test
    void deveLancarExcecaoQuandoMusicaNaoForEncontrada() {
        final MusicaRepository repository = mock(MusicaRepository.class);
        final MusicaMapper musicaMapper = mock(MusicaMapper.class);
        final BuscarMusicaUseCase useCase = new BuscarMusicaUseCase(repository, musicaMapper);
        final UUID id = UUID.randomUUID();

        when(repository.acharPeloId(id)).thenReturn(Optional.empty());

        final RuntimeException exception = assertThrows(RuntimeException.class, () -> useCase.execute(id));

        assertEquals("Música não encontrada.", exception.getMessage());
    }
}