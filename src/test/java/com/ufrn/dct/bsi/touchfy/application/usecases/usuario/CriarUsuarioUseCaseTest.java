package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.CriarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import com.ufrn.dct.bsi.touchfy.infrastructure.security.PasswordMaker;
import com.ufrn.dct.bsi.touchfy.shared.models.Email;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CriarUsuarioUseCaseTest {

    private UsuarioRepository repository;
    private UsuarioMapper mapper;
    private PasswordMaker passwordMaker;

    private CriarUsuarioUseCase useCase;

    @BeforeEach
    void setup() {
        repository = mock(UsuarioRepository.class);
        mapper = mock(UsuarioMapper.class);
        passwordMaker = mock(PasswordMaker.class);

        useCase = new CriarUsuarioUseCase(repository, mapper, passwordMaker);
    }

    private Usuario usuarioMock() {
        return new Usuario(
                UUID.randomUUID(),
                "Nome",
                "user",
                "senha",
                new Email("teste@email.com"),
                null,
                false,
                new Date()
        );
    }

    private CriarUsuarioRequest requestValido() {
        return new CriarUsuarioRequest(
                "Nome",
                "user",
                "senha",
                "senha",
                "teste@email.com",
                LocalDate.now()
        );
    }

    @Test
    void deveCriarUsuarioComSucesso() {
        var request = requestValido();
        var usuario = usuarioMock();

        when(mapper.toDomain(request)).thenReturn(usuario);
        when(passwordMaker.execute("senha")).thenReturn("hash");

        useCase.execute(request);

        assertEquals("hash", usuario.getSenha());
        verify(repository).salvar(usuario);
    }

    @Test
    void deveLancarErroQuandoSenhaForNula() {
        var request = new CriarUsuarioRequest(
                "Nome",
                "user",
                null,
                null,
                "teste@email.com",
                LocalDate.now()
        );

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));
    }

    @Test
    void deveLancarErroQuandoSenhasForemDiferentes() {
        var request = new CriarUsuarioRequest(
                "Nome",
                "user",
                "senha1",
                "senha2",
                "teste@email.com",
                LocalDate.now()
        );

        assertThrows(IllegalArgumentException.class, () -> useCase.execute(request));
    }

    @Test
    void deveGerarHashAntesDeSalvar() {
        var request = requestValido();
        var usuario = usuarioMock();

        when(mapper.toDomain(request)).thenReturn(usuario);
        when(passwordMaker.execute("senha")).thenReturn("hashSeguro");

        useCase.execute(request);

        verify(passwordMaker).execute("senha");
        verify(repository).salvar(usuario);
    }
}