package com.ufrn.dct.bsi.touchfy.application.usecases.arquivo;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ufrn.dct.bsi.touchfy.infrastructure.storage.FileStorageService;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoRecuperadoResponse;

class BuscarArquivoUseCaseTest {

    private FileStorageService fileStorageService;
    private BuscarArquivoUseCase useCase;

    @BeforeEach
    void setUp() {
        fileStorageService = mock(FileStorageService.class);
        useCase = new BuscarArquivoUseCase(fileStorageService);
    }

    @Test
    void deveDelegarBuscaParaStorageComCaminhoCorreto() {
        final String caminho = "perfil/uuid.png";
        final byte[] conteudo = "conteudo".getBytes();
        final ArquivoRecuperadoResponse responseEsperado = new ArquivoRecuperadoResponse(
                "uuid.png",
                caminho,
                "image/png",
                conteudo
        );

        when(fileStorageService.retrieve(caminho)).thenReturn(responseEsperado);

        final ArquivoRecuperadoResponse resultado = useCase.execute(caminho);

        assertEquals(responseEsperado.nome(), resultado.nome());
        assertEquals(responseEsperado.caminhoDoArquivo(), resultado.caminhoDoArquivo());
        assertEquals(responseEsperado.contentType(), resultado.contentType());
        assertArrayEquals(responseEsperado.conteudo(), resultado.conteudo());
        verify(fileStorageService, times(1)).retrieve(caminho);
    }
}