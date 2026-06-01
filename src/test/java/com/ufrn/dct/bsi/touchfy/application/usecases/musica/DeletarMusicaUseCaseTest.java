package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.DeletarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeletarMusicaUseCaseTest {

    @Test
    void deveDeletarMusicaEArquivoQuandoRegistroExistir() {
        final MusicaRepository repository = mock(MusicaRepository.class);
        final DeletarArquivoUseCase deletarArquivoUseCase = mock(DeletarArquivoUseCase.class);
        final DeletarMusicaUseCase useCase = new DeletarMusicaUseCase(repository, deletarArquivoUseCase);
        final UUID id = UUID.randomUUID();
        final Musica musica = Musica.builder()
                .id(id)
                .nome("Tempo Perdido")
                .caminhoDoArquivo("musicas/arquivo.mp3")
                .build();

        when(repository.acharPeloId(id)).thenReturn(Optional.of(musica));

        useCase.execute(id);

        verify(deletarArquivoUseCase, times(1)).execute("musicas/arquivo.mp3");
        verify(repository, times(1)).deletar(id);
    }

    @Test
    void deveLancarExcecaoQuandoIdForNulo() {
        final MusicaRepository repository = mock(MusicaRepository.class);
        final DeletarArquivoUseCase deletarArquivoUseCase = mock(DeletarArquivoUseCase.class);
        final DeletarMusicaUseCase useCase = new DeletarMusicaUseCase(repository, deletarArquivoUseCase);

        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));

        assertEquals("ID é obrigatório.", exception.getMessage());
        verify(repository, never()).acharPeloId(org.mockito.ArgumentMatchers.any());
    }
}