package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogoutUseCaseTest {

    private RefreshTokenRepository refreshTokenRepository;

    private LogoutUseCase useCase;

    @BeforeEach
    void setUp() {
        refreshTokenRepository = mock(RefreshTokenRepository.class);

        useCase = new LogoutUseCase(refreshTokenRepository);
    }

    @Test
    void deveRevogarTokenComSucesso() {
        final String token = "refresh.token";

        assertDoesNotThrow(() -> useCase.execute(token));

        verify(refreshTokenRepository).revogar(token);
    }

    @Test
    void deveLancarExcecaoQuandoTokenForNull() {
        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> useCase.execute(null)
        );

        assertEquals("Token inválido", exception.getMessage());

        verifyNoInteractions(refreshTokenRepository);
    }

    @Test
    void devePropagarExcecaoDoRepositorio() {
        final String token = "refresh.token";

        doThrow(new RuntimeException("Token não encontrado"))
                .when(refreshTokenRepository)
                .revogar(token);

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> useCase.execute(token)
        );

        assertEquals("Token não encontrado", exception.getMessage());

        verify(refreshTokenRepository).revogar(token);
    }
}