package com.ufrn.dct.bsi.touchfy.adapters.outbound.security;

import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.shared.models.Email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceImplTest {

    private TokenServiceImpl tokenService;

    private final String secret = "V80Kerm/UnTdNnse83DzMkGWnXASWkUW1VJGM4+4xTM=";
    private final long expiration = 1000 * 60 * 60;

    @BeforeEach
    void setUp() {
        tokenService = new TokenServiceImpl(secret, expiration);
    }

    @Test
    void deveGerarTokenValido() {
        final Usuario usuario = gerarUsuario(null);
        final String token = tokenService.generateToken(usuario);

        assertNotNull(token);
        assertFalse(token.isBlank());
    }

    @Test
    void deveExtrairSubjectDoToken() {
        final UUID id = UUID.randomUUID();
        final Usuario usuario = gerarUsuario(id);
        final String token = tokenService.generateToken(usuario);
        final String subject = tokenService.getSubject(token);

        assertEquals(usuario.getNomeUsuario(), subject);
    }

    @Test
    void deveGerarTokenDiferenteParaUsuariosDiferentes() {
        final Usuario usuario1 = gerarUsuario(null);
        final Usuario usuario2 = gerarUsuario(null);
        final String token1 = tokenService.generateToken(usuario1);
        final String token2 = tokenService.generateToken(usuario2);

        assertNotEquals(token1, token2);
    }

    @Test
    void deveFalharAoValidarTokenInvalido() {
        final String tokenInvalido = "token.fake.invalido";

        assertThrows(Exception.class, () -> {
            tokenService.getSubject(tokenInvalido);
        });
    }

    private Usuario gerarUsuario(final UUID id) {
        final Random random = new Random();
        final String username = String.format("user-%d", random.nextInt());
        return new Usuario(
                id != null ? id : UUID.randomUUID(),
                "Usuário",
                username,
                "minhasenha",
                new Email(String.format("%s@test.com", username)),
                null,
                Boolean.TRUE,
                new Date()
        );
    }
}