package com.ufrn.dct.bsi.touchfy.adapters.outbound.security;

import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceImplTest {

    private TokenServiceImpl tokenService;

    private final String accessSecret =
            Base64.getEncoder().encodeToString("12345678901234567890123456789012".getBytes());

    private final String refreshSecret =
            Base64.getEncoder().encodeToString("abcdefghijklmnopqrstuvwxyz123456".getBytes());

    private final long accessExpiration = 1000 * 60 * 60;
    private final long refreshExpiration = 1000 * 60 * 60 * 24;

    @BeforeEach
    void setUp() {
        tokenService = new TokenServiceImpl(accessSecret, refreshSecret, accessExpiration, refreshExpiration);
    }

    @Test
    void deveGerarAccessTokenValido() {
        final Usuario usuario = gerarUsuario(null);

        final String token = tokenService.generateAccessToken(usuario);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void deveGerarRefreshTokenValido() {
        final Usuario usuario = gerarUsuario(null);

        final String token = tokenService.generateRefreshToken(usuario);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void deveExtrairSubjectDoAccessToken() {
        final Usuario usuario = gerarUsuario(UUID.randomUUID());

        final String token = tokenService.generateAccessToken(usuario);

        final String subject = tokenService.getSubject(token);

        assertEquals(usuario.getNomeUsuario(), subject);
    }

    @Test
    void deveExtrairSubjectDoRefreshToken() {
        final Usuario usuario = gerarUsuario(UUID.randomUUID());

        final String token = tokenService.generateRefreshToken(usuario);

        final String subject = tokenService.getRefreshSubject(token);

        assertEquals(usuario.getNomeUsuario(), subject);
    }

    @Test
    void deveGerarTokensDiferentesParaUsuariosDiferentes() {
        final Usuario usuario1 = gerarUsuario(null);
        final Usuario usuario2 = gerarUsuario(null);

        final String token1 = tokenService.generateAccessToken(usuario1);
        final String token2 = tokenService.generateAccessToken(usuario2);

        assertNotEquals(token1, token2);
    }

    @Test
    void deveValidarAccessTokenValido() {
        final Usuario usuario = gerarUsuario(null);

        final String token = tokenService.generateAccessToken(usuario);

        assertTrue(tokenService.isTokenValid(token));
    }

    @Test
    void deveValidarRefreshTokenValido() {
        final Usuario usuario = gerarUsuario(null);

        final String token = tokenService.generateRefreshToken(usuario);

        assertTrue(tokenService.isRefreshTokenValid(token));
    }

    @Test
    void deveRetornarFalseParaAccessTokenInvalido() {
        final String tokenInvalido = "token.fake.invalido";

        assertFalse(tokenService.isTokenValid(tokenInvalido));
    }

    @Test
    void deveRetornarFalseParaRefreshTokenInvalido() {
        final String tokenInvalido = "token.fake.invalido";

        assertFalse(tokenService.isRefreshTokenValid(tokenInvalido));
    }

    @Test
    void deveFalharAoExtrairSubjectDeTokenInvalido() {
        final String tokenInvalido = "token.fake.invalido";

        assertThrows(JwtException.class, () -> tokenService.getSubject(tokenInvalido));
    }

    @Test
    void deveFalharAoExtrairSubjectDeRefreshTokenInvalido() {
        final String tokenInvalido = "token.fake.invalido";

        assertThrows(JwtException.class, () -> tokenService.getRefreshSubject(tokenInvalido));
    }

    private Usuario gerarUsuario(final UUID id) {
        final Random random = new Random();

        final String username = String.format("user-%d", random.nextInt());

        return new Usuario(id != null ? id : UUID.randomUUID(), "Usuário", username, "minhasenha",
                new Email(String.format("%s@test.com", username)), null, Boolean.TRUE, new Date());
    }
}