package com.ufrn.dct.bsi.touchfy.adapters.outbound.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PasswordEncoderImplTest {

    @Mock
    private org.springframework.security.crypto.password.PasswordEncoder springPasswordEncoder;

    private PasswordEncoderImpl passwordEncoderImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoderImpl = new PasswordEncoderImpl(springPasswordEncoder);
    }

    @Test
    void deveRetornarTrueQuandoSenhasCoincidem() {
        final String senhaPura = "123456";
        final String senhaCriptografada = "hash";

        when(springPasswordEncoder.matches(senhaPura, senhaCriptografada))
                .thenReturn(true);

        Boolean resultado = passwordEncoderImpl.matches(senhaPura, senhaCriptografada);

        assertTrue(resultado);
        verify(springPasswordEncoder).matches(senhaPura, senhaCriptografada);
    }

    @Test
    void deveRetornarFalseQuandoSenhasNaoCoincidem() {
        final String senhaPura = "123456";
        final String senhaCriptografada = "hash";

        when(springPasswordEncoder.matches(senhaPura, senhaCriptografada))
                .thenReturn(false);

        final Boolean resultado = passwordEncoderImpl.matches(senhaPura, senhaCriptografada);

        assertFalse(resultado);
        verify(springPasswordEncoder).matches(senhaPura, senhaCriptografada);
    }

    @Test
    void deveDelegarChamadaParaPasswordEncoderDoSpring() {
        final String senhaPura = "abc";
        final String senhaCriptografada = "xyz";

        passwordEncoderImpl.matches(senhaPura, senhaCriptografada);

        verify(springPasswordEncoder, times(1))
                .matches(senhaPura, senhaCriptografada);
    }
}