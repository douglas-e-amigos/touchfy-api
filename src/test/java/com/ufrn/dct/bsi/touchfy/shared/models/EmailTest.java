package com.ufrn.dct.bsi.touchfy.shared.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailTest {
    @Test
    void deveCriarEmailValido() {
        Email email = new Email("teste@email.com");

        assertEquals("teste@email.com", email.getValue());
    }

    @Test
    void deveLancarExcecaoQuandoEmailForNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Email(null);
        });
    }

    @Test
    void deveLancarExcecaoQuandoEmailForInvalido() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Email("email-invalido");
        });
    }

    @Test
    void deveLancarExcecaoQuandoEmailSemDominio() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Email("teste@");
        });
    }

    @Test
    void deveLancarExcecaoQuandoEmailSemUsuario() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Email("@dominio.com");
        });
    }
}
