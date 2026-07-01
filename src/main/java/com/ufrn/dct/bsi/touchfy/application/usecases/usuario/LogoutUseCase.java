package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.RefreshTokenRepository;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.NaoAutenticadoException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LogoutUseCase {
    private final RefreshTokenRepository refreshTokenRepository;

    public void execute(final String token) {
        if (token == null) {
            throw new NaoAutenticadoException("Token inválido");
        }

        refreshTokenRepository.revogar(token);
    }
}
