package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.MusicaMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.MusicaResponse;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ConsultarMusicasDoArtistaUseCaseTest {

  @Test
  void deveConsultarMusicasPeloIdDoArtista() {
    final MusicaRepository repository = mock(MusicaRepository.class);
    final MusicaMapper musicaMapper = mock(MusicaMapper.class);
    final ConsultarMusicasDoArtistaUseCase useCase =
        new ConsultarMusicasDoArtistaUseCase(repository, musicaMapper);
    final UUID artistaId = UUID.randomUUID();
    final Musica musica =
        Musica.builder()
            .id(UUID.randomUUID())
            .nome("Tempo Perdido")
            .caminhoDoArquivo("musicas/arquivo.mp3")
            .criadoPor(artistaId)
            .artistaNome("João")
            .artistaNomeUsuario("joao")
            .build();
    final MusicaResponse response =
        new MusicaResponse(
            musica.getId(),
            musica.getNome(),
            musica.getCaminhoDoArquivo(),
            null,
            artistaId,
            "João",
            "joao",
            List.of(),
            List.of());

    when(repository.consultarPorCriadoPor(artistaId)).thenReturn(List.of(musica));
    when(musicaMapper.toResponse(musica)).thenReturn(response);

    final List<MusicaResponse> resultado = useCase.execute(artistaId);

    assertEquals(List.of(response), resultado);
    verify(repository, times(1)).consultarPorCriadoPor(artistaId);
    verify(musicaMapper, times(1)).toResponse(musica);
  }

  @Test
  void deveLancarExcecaoQuandoIdDoArtistaForNulo() {
    final MusicaRepository repository = mock(MusicaRepository.class);
    final MusicaMapper musicaMapper = mock(MusicaMapper.class);
    final ConsultarMusicasDoArtistaUseCase useCase =
        new ConsultarMusicasDoArtistaUseCase(repository, musicaMapper);

    final IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null));

    assertEquals("ID do artista é obrigatório.", exception.getMessage());
    verify(repository, never()).consultarPorCriadoPor(org.mockito.ArgumentMatchers.any());
  }
}
