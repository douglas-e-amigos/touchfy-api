package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import com.ufrn.dct.bsi.touchfy.infrastructure.security.PasswordEncoder;
import com.ufrn.dct.bsi.touchfy.infrastructure.security.TokenService;
import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AutenticarUsuarioUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private TokenService tokenService;
    private UsuarioMapper usuarioMapper;

    private AutenticarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        tokenService = mock(TokenService.class);
        usuarioMapper = mock(UsuarioMapper.class);

        useCase = new AutenticarUsuarioUseCase(
                usuarioRepository,
                passwordEncoder,
                tokenService,
                usuarioMapper
        );
    }

    private UsuarioEntity criarUsuarioEntity() {
        return UsuarioEntity.builder()
                .id(UUID.randomUUID())
                .nome("Nome")
                .nomeUsuario("usuario_teste")
                .senha("senha_hash")
                .email(new Email("teste@email.com"))
                .dataNascimento(LocalDate.now())
                .build();
    }

    private Usuario criarUsuarioDomain() {
        return Usuario.builder()
                .id(UUID.randomUUID())
                .nome("Nome")
                .nomeUsuario("usuario_teste")
                .senha("senha_hash")
                .email(new Email("teste@email.com"))
                .dataNascimento(new Date())
                .build();
    }

    @Test
    void deveRetornarTokenQuandoCredenciaisValidas() {
        final String username = "usuario_teste";
        final String senha = "senha";
        final String tokenEsperado = "token.jwt";

        final UsuarioEntity entity = criarUsuarioEntity();
        final Usuario usuarioDomain = criarUsuarioDomain();

        when(usuarioRepository.acharPeloNomeDeUsuario(username))
                .thenReturn(Optional.of(entity));

        when(passwordEncoder.matches(senha, entity.getSenha()))
                .thenReturn(true);

        when(usuarioMapper.toDomain(entity))
                .thenReturn(usuarioDomain);

        when(tokenService.generateToken(usuarioDomain))
                .thenReturn(tokenEsperado);

        final String resultado = useCase.execute(username, senha);

        assertEquals(tokenEsperado, resultado);

        verify(usuarioRepository).acharPeloNomeDeUsuario(username);
        verify(passwordEncoder).matches(senha, entity.getSenha());
        verify(usuarioMapper).toDomain(entity);
        verify(tokenService).generateToken(usuarioDomain);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        final String username = "inexistente";
        final String senha = "senha";

        when(usuarioRepository.acharPeloNomeDeUsuario(username))
                .thenReturn(Optional.empty());

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> useCase.execute(username, senha)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());

        verify(usuarioRepository).acharPeloNomeDeUsuario(username);
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(tokenService);
        verifyNoInteractions(usuarioMapper);
    }

    @Test
    void deveLancarExcecaoQuandoSenhaInvalida() {
        final String username = "usuario_teste";
        final String senha = "senha_errada";

        final UsuarioEntity entity = criarUsuarioEntity();

        when(usuarioRepository.acharPeloNomeDeUsuario(username))
                .thenReturn(Optional.of(entity));

        when(passwordEncoder.matches(senha, entity.getSenha()))
                .thenReturn(false);

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> useCase.execute(username, senha)
        );

        assertEquals("Credenciais inválidas", exception.getMessage());

        verify(usuarioRepository).acharPeloNomeDeUsuario(username);
        verify(passwordEncoder).matches(senha, entity.getSenha());
        verifyNoInteractions(tokenService);
        verifyNoInteractions(usuarioMapper);
    }

    @Test
    void naoDeveGerarTokenSemValidarSenha() {
        final String username = "usuario_teste";
        final String senha = "senha_errada";

        final UsuarioEntity entity = criarUsuarioEntity();

        when(usuarioRepository.acharPeloNomeDeUsuario(username))
                .thenReturn(Optional.of(entity));

        when(passwordEncoder.matches(senha, entity.getSenha()))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> useCase.execute(username, senha));

        verify(tokenService, never()).generateToken(any());
    }
}
