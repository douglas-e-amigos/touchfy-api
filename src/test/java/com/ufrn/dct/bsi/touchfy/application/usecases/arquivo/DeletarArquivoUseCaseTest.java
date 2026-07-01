package com.ufrn.dct.bsi.touchfy.application.usecases.arquivo;

import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.infrastructure.storage.FileStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeletarArquivoUseCaseTest {

  private FileStorageService fileStorageService;
  private DeletarArquivoUseCase useCase;

  @BeforeEach
  void setUp() {
    fileStorageService = mock(FileStorageService.class);
    useCase = new DeletarArquivoUseCase(fileStorageService);
  }

  @Test
  void deveChamarDeleteComCaminhoCorreto() {
    final String caminho = "perfil/uuid.png";

    useCase.execute(caminho);

    verify(fileStorageService, times(1)).delete(caminho);
  }

  @Test
  void deveDelegarParaFileStorageService() {
    final String caminho = "musicas/uuid.mp3";

    useCase.execute(caminho);

    verify(fileStorageService, only()).delete(caminho);
  }
}
