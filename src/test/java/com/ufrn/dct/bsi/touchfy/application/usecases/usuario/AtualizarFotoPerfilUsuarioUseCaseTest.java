package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.AtualizarFotoPerfilUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.DeletarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.UploadArquivoUseCase;

import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoArmazenamentoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AtualizarFotoPerfilUsuarioUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private UploadArquivoUseCase uploadArquivoUseCase;
    private DeletarArquivoUseCase deletarArquivoUseCase;

    private AtualizarFotoPerfilUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        uploadArquivoUseCase = mock(UploadArquivoUseCase.class);
        deletarArquivoUseCase = mock(DeletarArquivoUseCase.class);

        useCase = new AtualizarFotoPerfilUsuarioUseCase(
                usuarioRepository,
                uploadArquivoUseCase,
                deletarArquivoUseCase
        );
    }

    @Test
    @DisplayName("Deve atualizar foto de perfil sem imagem anterior")
    void deveAtualizarFotoPerfilSemImagemAnterior() {
        final UUID usuarioId = UUID.randomUUID();

        final UsuarioEntity usuario =
                mock(UsuarioEntity.class);

        when(usuario.getNomeUsuario())
                .thenReturn("joao");

        when(usuario.getCaminhoDaImagemDePerfil())
                .thenReturn(null);

        when(usuarioRepository.acharPeloId(usuarioId))
                .thenReturn(Optional.of(usuario));

        final MultipartFile foto =
                mock(MultipartFile.class);

        final AtualizarFotoPerfilUsuarioRequest request =
                new AtualizarFotoPerfilUsuarioRequest(foto);

        final ArquivoArmazenamentoResponse response =
                mock(ArquivoArmazenamentoResponse.class);

        when(response.caminhoDoArquivo())
                .thenReturn("usuarios/joao/perfil/foto.png");

        when(uploadArquivoUseCase.execute(
                any(),
                anyString()
        )).thenReturn(response);

        useCase.execute(usuarioId, request);

        verify(deletarArquivoUseCase, never())
                .execute(anyString());

        verify(uploadArquivoUseCase)
                .execute(
                        foto,
                        "usuarios/joao/perfil"
                );

        verify(usuarioRepository)
                .atualizarFotoPerfilUsuario(
                        usuario,
                        "usuarios/joao/perfil/foto.png"
                );
    }

    @Test
    @DisplayName("Deve atualizar foto de perfil removendo imagem anterior")
    void deveAtualizarFotoPerfilRemovendoImagemAnterior() {
        final UUID usuarioId = UUID.randomUUID();

        final UsuarioEntity usuario =
                mock(UsuarioEntity.class);

        when(usuario.getNomeUsuario())
                .thenReturn("maria");

        when(usuario.getCaminhoDaImagemDePerfil())
                .thenReturn("usuarios/maria/perfil/antiga.png");

        when(usuarioRepository.acharPeloId(usuarioId))
                .thenReturn(Optional.of(usuario));

        final MultipartFile foto =
                mock(MultipartFile.class);

        final AtualizarFotoPerfilUsuarioRequest request =
                new AtualizarFotoPerfilUsuarioRequest(foto);

        final ArquivoArmazenamentoResponse response =
                mock(ArquivoArmazenamentoResponse.class);

        when(response.caminhoDoArquivo())
                .thenReturn("usuarios/maria/perfil/nova.png");

        when(uploadArquivoUseCase.execute(
                any(),
                anyString()
        )).thenReturn(response);

        useCase.execute(usuarioId, request);

        verify(deletarArquivoUseCase)
                .execute("usuarios/maria/perfil/antiga.png");

        verify(uploadArquivoUseCase)
                .execute(
                        foto,
                        "usuarios/maria/perfil"
                );

        verify(usuarioRepository)
                .atualizarFotoPerfilUsuario(
                        usuario,
                        "usuarios/maria/perfil/nova.png"
                );
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não existir")
    void deveLancarExcecaoQuandoUsuarioNaoExistir() {
        final UUID usuarioId = UUID.randomUUID();

        when(usuarioRepository.acharPeloId(usuarioId))
                .thenReturn(Optional.empty());

        final AtualizarFotoPerfilUsuarioRequest request =
                new AtualizarFotoPerfilUsuarioRequest(
                        mock(MultipartFile.class)
                );

        final RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> useCase.execute(usuarioId, request)
                );

        verify(uploadArquivoUseCase, never())
                .execute(any(), anyString());

        verify(deletarArquivoUseCase, never())
                .execute(anyString());

        verify(usuarioRepository, never())
                .atualizarFotoPerfilUsuario(any(), anyString());

        org.junit.jupiter.api.Assertions.assertEquals(
                "Usuário não encontrado.",
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("Deve propagar exceção ao fazer upload")
    void devePropagarExcecaoAoFazerUpload() {
        final UUID usuarioId = UUID.randomUUID();

        final UsuarioEntity usuario =
                mock(UsuarioEntity.class);

        when(usuario.getNomeUsuario())
                .thenReturn("pedro");

        when(usuario.getCaminhoDaImagemDePerfil())
                .thenReturn(null);

        when(usuarioRepository.acharPeloId(usuarioId))
                .thenReturn(Optional.of(usuario));

        final AtualizarFotoPerfilUsuarioRequest request =
                new AtualizarFotoPerfilUsuarioRequest(
                        mock(MultipartFile.class)
                );

        when(uploadArquivoUseCase.execute(
                any(),
                anyString()
        )).thenThrow(
                new RuntimeException("Erro no upload")
        );

        final RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> useCase.execute(usuarioId, request)
                );

        verify(usuarioRepository, never())
                .atualizarFotoPerfilUsuario(any(), anyString());

        org.junit.jupiter.api.Assertions.assertEquals(
                "Erro no upload",
                exception.getMessage()
        );
    }

    @Test
    @DisplayName("Deve propagar exceção ao deletar imagem anterior")
    void devePropagarExcecaoAoDeletarImagemAnterior() {
        final UUID usuarioId = UUID.randomUUID();

        final UsuarioEntity usuario =
                mock(UsuarioEntity.class);

        when(usuario.getNomeUsuario())
                .thenReturn("ana");

        when(usuario.getCaminhoDaImagemDePerfil())
                .thenReturn("imagem-antiga.png");

        when(usuarioRepository.acharPeloId(usuarioId))
                .thenReturn(Optional.of(usuario));

        doThrow(new RuntimeException("Erro ao deletar"))
                .when(deletarArquivoUseCase)
                .execute(anyString());

        final AtualizarFotoPerfilUsuarioRequest request =
                new AtualizarFotoPerfilUsuarioRequest(
                        mock(MultipartFile.class)
                );

        final RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> useCase.execute(usuarioId, request)
                );

        verify(uploadArquivoUseCase, never())
                .execute(any(), anyString());

        verify(usuarioRepository, never())
                .atualizarFotoPerfilUsuario(any(), anyString());

        org.junit.jupiter.api.Assertions.assertEquals(
                "Erro ao deletar",
                exception.getMessage()
        );
    }
}
