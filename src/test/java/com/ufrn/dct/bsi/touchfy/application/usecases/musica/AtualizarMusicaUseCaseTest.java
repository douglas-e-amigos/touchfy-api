package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarMusicaRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.DeletarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.UploadArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoArmazenamentoResponse;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.AcessoNegadoException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.AuditorAware;
import org.springframework.mock.web.MockMultipartFile;

class AtualizarMusicaUseCaseTest {

  @Test
  void deveAtualizarArquivoQuandoNovoMp3ForInformado() {
    final MusicaRepository repository = mock(MusicaRepository.class);
    final UploadArquivoUseCase uploadArquivoUseCase = mock(UploadArquivoUseCase.class);
    final DeletarArquivoUseCase deletarArquivoUseCase = mock(DeletarArquivoUseCase.class);
    final UUID artistaId = UUID.randomUUID();
    final AuditorAware<UUID> auditorAware = () -> Optional.of(artistaId);
    final AtualizarMusicaUseCase useCase =
        new AtualizarMusicaUseCase(
            repository, uploadArquivoUseCase, deletarArquivoUseCase, auditorAware);
    final UUID id = UUID.randomUUID();
    final MockMultipartFile arquivo =
        new MockMultipartFile("arquivo", "tempo-perdido.mp3", "audio/mpeg", "conteudo".getBytes());
    final AtualizarMusicaRequest request =
        new AtualizarMusicaRequest(
            "Tempo Perdido",
            "Nova letra",
            List.of(UUID.randomUUID()),
            List.of(UUID.randomUUID()),
            arquivo);
    final Musica musica =
        Musica.builder()
            .id(id)
            .nome("Tempo Perdido")
            .caminhoDoArquivo("musicas/antiga/tempo-perdido.mp3")
            .criadoPor(artistaId)
            .build();
    final ArquivoArmazenamentoResponse response =
        new ArquivoArmazenamentoResponse(
            "tempo-perdido.mp3", "musicas/" + id + "/tempo-perdido.mp3", "mp3", 8.0);

    when(repository.acharPeloId(id)).thenReturn(Optional.of(musica));
    when(uploadArquivoUseCase.execute(arquivo, "musicas/" + id)).thenReturn(response);

    useCase.execute(id, request);

    verify(deletarArquivoUseCase, times(1)).execute("musicas/antiga/tempo-perdido.mp3");
    verify(uploadArquivoUseCase, times(1)).execute(arquivo, "musicas/" + id);
    verify(repository, times(1)).atualizar(id, request, "musicas/" + id + "/tempo-perdido.mp3");
  }

  @Test
  void deveAtualizarSemNovoArquivoQuandoArquivoNaoForInformado() {
    final MusicaRepository repository = mock(MusicaRepository.class);
    final UploadArquivoUseCase uploadArquivoUseCase = mock(UploadArquivoUseCase.class);
    final DeletarArquivoUseCase deletarArquivoUseCase = mock(DeletarArquivoUseCase.class);
    final UUID artistaId = UUID.randomUUID();
    final AuditorAware<UUID> auditorAware = () -> Optional.of(artistaId);
    final AtualizarMusicaUseCase useCase =
        new AtualizarMusicaUseCase(
            repository, uploadArquivoUseCase, deletarArquivoUseCase, auditorAware);
    final UUID id = UUID.randomUUID();
    final AtualizarMusicaRequest request =
        new AtualizarMusicaRequest("Tempo Perdido", null, null, null, null);
    final Musica musica =
        Musica.builder()
            .id(id)
            .nome("Tempo Perdido")
            .caminhoDoArquivo("musicas/antiga/tempo-perdido.mp3")
            .criadoPor(artistaId)
            .build();

    when(repository.acharPeloId(id)).thenReturn(Optional.of(musica));

    useCase.execute(id, request);

    verify(deletarArquivoUseCase, never()).execute(org.mockito.ArgumentMatchers.anyString());
    verify(uploadArquivoUseCase, never())
        .execute(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString());
    verify(repository, times(1)).atualizar(id, request, null);
  }

  @Test
  void deveBloquearAtualizacaoQuandoUsuarioNaoForCriador() {
    final MusicaRepository repository = mock(MusicaRepository.class);
    final UploadArquivoUseCase uploadArquivoUseCase = mock(UploadArquivoUseCase.class);
    final DeletarArquivoUseCase deletarArquivoUseCase = mock(DeletarArquivoUseCase.class);
    final AuditorAware<UUID> auditorAware = () -> Optional.of(UUID.randomUUID());
    final AtualizarMusicaUseCase useCase =
        new AtualizarMusicaUseCase(
            repository, uploadArquivoUseCase, deletarArquivoUseCase, auditorAware);
    final UUID id = UUID.randomUUID();
    final AtualizarMusicaRequest request =
        new AtualizarMusicaRequest("Tempo Perdido", null, null, null, null);
    final Musica musica =
        Musica.builder()
            .id(id)
            .nome("Tempo Perdido")
            .caminhoDoArquivo("musicas/antiga/tempo-perdido.mp3")
            .criadoPor(UUID.randomUUID())
            .build();

    when(repository.acharPeloId(id)).thenReturn(Optional.of(musica));

    final AcessoNegadoException exception =
        assertThrows(AcessoNegadoException.class, () -> useCase.execute(id, request));

    assertEquals("Usuário não tem permissão para alterar esta música.", exception.getMessage());
    verify(deletarArquivoUseCase, never()).execute(org.mockito.ArgumentMatchers.anyString());
    verify(uploadArquivoUseCase, never())
        .execute(org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.anyString());
    verify(repository, never())
        .atualizar(
            org.mockito.ArgumentMatchers.any(),
            org.mockito.ArgumentMatchers.any(),
            org.mockito.ArgumentMatchers.any());
  }

  @Test
  void deveLancarExcecaoQuandoIdForNulo() {
    final MusicaRepository repository = mock(MusicaRepository.class);
    final UploadArquivoUseCase uploadArquivoUseCase = mock(UploadArquivoUseCase.class);
    final DeletarArquivoUseCase deletarArquivoUseCase = mock(DeletarArquivoUseCase.class);
    final AuditorAware<UUID> auditorAware = Optional::<UUID>empty;
    final AtualizarMusicaUseCase useCase =
        new AtualizarMusicaUseCase(
            repository, uploadArquivoUseCase, deletarArquivoUseCase, auditorAware);

    final var exception =
        assertThrows(IllegalArgumentException.class, () -> useCase.execute(null, null));

    assertEquals("Os dados da música são obrigatórios.", exception.getMessage());
  }
}
