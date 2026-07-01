package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.StreamingMusicaResponse;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.BuscarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoRecuperadoResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

class StreamingMusicaUseCaseTest {

  @Test
  void deveRetornarFaixaSolicitadaDaMusica() {
    final MusicaRepository repository = mock(MusicaRepository.class);
    final BuscarArquivoUseCase buscarArquivoUseCase = mock(BuscarArquivoUseCase.class);
    final StreamingMusicaUseCase useCase =
        new StreamingMusicaUseCase(repository, buscarArquivoUseCase);
    final UUID id = UUID.randomUUID();
    final byte[] arquivo = "abcdefghij".getBytes(StandardCharsets.UTF_8);
    final Musica musica =
        Musica.builder()
            .id(id)
            .nome("Música teste")
            .caminhoDoArquivo("musicas/teste.mp3")
            .tags(List.of())
            .generosMusicais(List.of())
            .build();

    when(repository.acharPeloId(id)).thenReturn(Optional.of(musica));
    when(buscarArquivoUseCase.execute("musicas/teste.mp3"))
        .thenReturn(
            new ArquivoRecuperadoResponse("teste.mp3", "musicas/teste.mp3", "audio/mpeg", arquivo));

    final StreamingMusicaResponse response = useCase.execute(id, "bytes=2-5");

    assertTrue(response.parcial());
    assertEquals(2, response.inicio());
    assertEquals(5, response.fim());
    assertEquals(10, response.tamanhoTotal());
    assertArrayEquals("cdef".getBytes(StandardCharsets.UTF_8), response.conteudo());
  }

  @Test
  void deveRetornarArquivoCompletoQuandoRangeNaoForInformado() {
    final MusicaRepository repository = mock(MusicaRepository.class);
    final BuscarArquivoUseCase buscarArquivoUseCase = mock(BuscarArquivoUseCase.class);
    final StreamingMusicaUseCase useCase =
        new StreamingMusicaUseCase(repository, buscarArquivoUseCase);
    final UUID id = UUID.randomUUID();
    final byte[] arquivo = "abc".getBytes(StandardCharsets.UTF_8);
    final Musica musica =
        Musica.builder()
            .id(id)
            .nome("Música teste")
            .caminhoDoArquivo("musicas/teste.mp3")
            .tags(List.of())
            .generosMusicais(List.of())
            .build();

    when(repository.acharPeloId(id)).thenReturn(Optional.of(musica));
    when(buscarArquivoUseCase.execute("musicas/teste.mp3"))
        .thenReturn(
            new ArquivoRecuperadoResponse("teste.mp3", "musicas/teste.mp3", "audio/mpeg", arquivo));

    final StreamingMusicaResponse response = useCase.execute(id, null);

    assertFalse(response.parcial());
    assertEquals(0, response.inicio());
    assertEquals(2, response.fim());
    assertEquals(3, response.tamanhoTotal());
    assertArrayEquals(arquivo, response.conteudo());
  }

  @Test
  void deveLancarExcecaoQuandoRangeForInvalido() {
    final MusicaRepository repository = mock(MusicaRepository.class);
    final BuscarArquivoUseCase buscarArquivoUseCase = mock(BuscarArquivoUseCase.class);
    final StreamingMusicaUseCase useCase =
        new StreamingMusicaUseCase(repository, buscarArquivoUseCase);
    final UUID id = UUID.randomUUID();
    final byte[] arquivo = "abc".getBytes(StandardCharsets.UTF_8);
    final Musica musica =
        Musica.builder()
            .id(id)
            .nome("Música teste")
            .caminhoDoArquivo("musicas/teste.mp3")
            .tags(List.of())
            .generosMusicais(List.of())
            .build();

    when(repository.acharPeloId(id)).thenReturn(Optional.of(musica));
    when(buscarArquivoUseCase.execute("musicas/teste.mp3"))
        .thenReturn(
            new ArquivoRecuperadoResponse("teste.mp3", "musicas/teste.mp3", "audio/mpeg", arquivo));

    final ResponseStatusException exception =
        assertThrows(ResponseStatusException.class, () -> useCase.execute(id, "bytes=5-10"));

    assertEquals(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE, exception.getStatusCode());
  }
}
