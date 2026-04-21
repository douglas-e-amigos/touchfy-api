package com.ufrn.dct.bsi.touchfy.adapters.outbound.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PasswordMakerImplTest {

    private PasswordEncoder passwordEncoder;
    private PasswordMakerImpl passwordMaker;

    @BeforeEach
    void setup() {
        passwordEncoder = mock(PasswordEncoder.class);
        passwordMaker = new PasswordMakerImpl(passwordEncoder);
    }

    @Test
    void deveDelegarParaPasswordEncoder() {
        when(passwordEncoder.encode("senha")).thenReturn("hash");
        final var result = passwordMaker.execute("senha");
        assertEquals("hash", result);
        verify(passwordEncoder).encode("senha");
    }

    @Test
    void deveRetornarHashDiferenteDaSenhaOriginal() {
        when(passwordEncoder.encode("senha")).thenReturn("hashSeguro");
        final var result = passwordMaker.execute("senha");
        assertEquals("hashSeguro", result);
    }
}