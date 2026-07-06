package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.BuscarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.DeletarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.UploadArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoArmazenamentoResponse;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoRecuperadoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class ArquivoControllerTest {

  private MockMvc mockMvc;
  private final BuscarArquivoUseCase buscarArquivoUseCase = mock(BuscarArquivoUseCase.class);
  private final UploadArquivoUseCase uploadArquivoUseCase = mock(UploadArquivoUseCase.class);
  private final DeletarArquivoUseCase deletarArquivoUseCase = mock(DeletarArquivoUseCase.class);

  @BeforeEach
  void setUp() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(
                new ArquivoController(
                    buscarArquivoUseCase, uploadArquivoUseCase, deletarArquivoUseCase))
            .build();
  }

  @Test
  void deveBuscarArquivo() throws Exception {
    when(buscarArquivoUseCase.execute("caminho/teste.txt"))
        .thenReturn(
            new ArquivoRecuperadoResponse(
                "arquivo.txt", "caminho/teste.txt", "text/plain", new byte[] {1, 2, 3}));
    mockMvc
        .perform(get("/arquivos").param("caminho", "caminho/teste.txt"))
        .andExpect(status().isOk())
        .andExpect(header().string("Content-Type", "text/plain"));
  }

  @Test
  void deveFazerUpload() throws Exception {
    final var file =
        new MockMultipartFile("file", "arquivo.txt", "text/plain", "conteudo".getBytes());
    when(uploadArquivoUseCase.execute(any(), anyString()))
        .thenReturn(mock(ArquivoArmazenamentoResponse.class));
    mockMvc.perform(multipart("/arquivos/upload/subdir").file(file)).andExpect(status().isOk());
    verify(uploadArquivoUseCase).execute(any(), eq("subdir"));
  }

  @Test
  void deveDeletarArquivo() throws Exception {
    mockMvc.perform(delete("/arquivos/subdir/arquivo.txt")).andExpect(status().isNoContent());
    verify(deletarArquivoUseCase).execute("subdir/arquivo.txt");
  }
}
