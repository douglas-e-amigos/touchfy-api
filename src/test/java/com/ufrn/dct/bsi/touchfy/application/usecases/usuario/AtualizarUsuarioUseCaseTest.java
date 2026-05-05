package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.AtualizarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;

class AtualizarUsuarioUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private AtualizarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        useCase = new AtualizarUsuarioUseCase(usuarioRepository);
    }

    private AtualizarUsuarioRequest criarRequest() {
        return new AtualizarUsuarioRequest(
                "Novo Nome",
                "novo_username",
                LocalDate.of(2000, 1, 1)
        );
    }

    @Test
    void deveChamarRepositoryComParametrosCorretos() {
        final UUID id = UUID.randomUUID();
        final AtualizarUsuarioRequest request = criarRequest();

        useCase.execute(id, request);

        verify(usuarioRepository, times(1))
                .atualizarUsuarioParcialmente(id, request);
    }

    @Test
    void deveDelegarSemAlterarParametros() {
        final UUID id = UUID.randomUUID();
        final AtualizarUsuarioRequest request = criarRequest();

        useCase.execute(id, request);

        verify(usuarioRepository).atualizarUsuarioParcialmente(id, request);
        verifyNoMoreInteractions(usuarioRepository);
    }
}
