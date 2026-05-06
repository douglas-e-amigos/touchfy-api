package com.ufrn.dct.bsi.touchfy.application.usecases.arquivo;

import com.ufrn.dct.bsi.touchfy.infrastructure.storage.FileStorageService;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoArmazenamentoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class UploadArquivoUseCaseTest {

    private FileStorageService fileStorageService;
    private UploadArquivoUseCase useCase;

    @BeforeEach
    void setUp() {
        fileStorageService = mock(FileStorageService.class);
        useCase = new UploadArquivoUseCase(fileStorageService);
    }

    @Test
    void deveChamarStoreComParametrosCorretos() {
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "foto.png",
                "image/png",
                "conteudo".getBytes()
        );

        final ArquivoArmazenamentoResponse responseEsperado = new ArquivoArmazenamentoResponse(
                "foto.png",
                "perfil/uuid.png",
                "png",
                8.0
        );

        when(fileStorageService.store(file, "perfil")).thenReturn(responseEsperado);

        final ArquivoArmazenamentoResponse resultado = useCase.execute(file, "perfil");

        assertEquals(responseEsperado, resultado);
        verify(fileStorageService, times(1)).store(file, "perfil");
    }

    @Test
    void deveRetornarResponseDoStorage() {
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "musica.mp3",
                "audio/mpeg",
                "conteudo".getBytes()
        );

        final ArquivoArmazenamentoResponse responseEsperado = new ArquivoArmazenamentoResponse(
                "musica.mp3",
                "musicas/uuid.mp3",
                "mp3",
                8.0
        );

        when(fileStorageService.store(file, "musicas")).thenReturn(responseEsperado);

        final ArquivoArmazenamentoResponse resultado = useCase.execute(file, "musicas");

        assertEquals(responseEsperado, resultado);
    }
}