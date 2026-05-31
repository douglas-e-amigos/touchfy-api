package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarMusicaRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.UploadArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoArmazenamentoResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CriarMusicaUseCaseTest {

    @Test
    void deveFazerUploadESalvarMusicaQuandoRequestForValido() {
        final MusicaRepository repository = mock(MusicaRepository.class);
        final UploadArquivoUseCase uploadArquivoUseCase = mock(UploadArquivoUseCase.class);
        final CriarMusicaUseCase useCase = new CriarMusicaUseCase(repository, uploadArquivoUseCase);
        final MockMultipartFile arquivo = new MockMultipartFile(
                "arquivo",
                "tempo-perdido.mp3",
                "audio/mpeg",
                "conteudo".getBytes()
        );
        final CriarMusicaRequest request = new CriarMusicaRequest(
                "Tempo Perdido",
                "Letra",
                List.of(UUID.randomUUID()),
                List.of(UUID.randomUUID()),
                arquivo
        );
        final ArquivoArmazenamentoResponse response = new ArquivoArmazenamentoResponse(
                "tempo-perdido.mp3",
                "musicas/123/tempo-perdido.mp3",
                "mp3",
                8.0
        );

        when(uploadArquivoUseCase.execute(arquivo, "musicas/123")).thenReturn(response);

        final ArgumentCaptor<String> diretorioCaptor = ArgumentCaptor.forClass(String.class);
        when(uploadArquivoUseCase.execute(org.mockito.ArgumentMatchers.eq(arquivo), diretorioCaptor.capture()))
                .thenReturn(response);

        useCase.execute(request);

        assertNotNull(diretorioCaptor.getValue());
        verify(uploadArquivoUseCase, times(1)).execute(org.mockito.ArgumentMatchers.eq(arquivo), org.mockito.ArgumentMatchers.anyString());
        verify(repository, times(1)).salvar(request, "musicas/123/tempo-perdido.mp3");
    }

    @Test
    void deveLancarExcecaoQuandoRequestForInvalido() {
        final MusicaRepository repository = mock(MusicaRepository.class);
        final UploadArquivoUseCase uploadArquivoUseCase = mock(UploadArquivoUseCase.class);
        final CriarMusicaUseCase useCase = new CriarMusicaUseCase(repository, uploadArquivoUseCase);

        final var exception = assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));

        assertEquals("Os dados da música são obrigatórios.", exception.getMessage());
        verify(uploadArquivoUseCase, never()).execute(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString());
        verify(repository, never()).salvar(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString());
    }
}