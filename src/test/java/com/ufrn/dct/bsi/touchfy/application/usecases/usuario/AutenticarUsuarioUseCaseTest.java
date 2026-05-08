package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.RefreshTokenRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AutenticarUsuarioUseCaseTest {

    private UsuarioRepository usuarioRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private PasswordEncoder passwordEncoder;
    private TokenService tokenService;
    private UsuarioMapper usuarioMapper;

    private AutenticarUsuarioUseCase useCase;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        tokenService = mock(TokenService.class);
        usuarioMapper = mock(UsuarioMapper.class);

        useCase = new AutenticarUsuarioUseCase(usuarioRepository, refreshTokenRepository, passwordEncoder,
                tokenService, usuarioMapper);
    }

    private UsuarioEntity criarUsuarioEntity() {
        return UsuarioEntity.builder().id(UUID.randomUUID()).nome("Nome").nomeUsuario("usuario_teste").senha(
                "senha_hash").email(new Email("teste@email.com")).dataNascimento(LocalDate.now()).build();
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
    void deveRetornarTokensQuandoCredenciaisValidas() {
        final String username = "usuario_teste";
        final String senha = "senha";

        final String accessTokenEsperado = "access.token";
        final String refreshTokenEsperado = "refresh.token";

        final UsuarioEntity entity = criarUsuarioEntity();
        final Usuario usuarioDomain = criarUsuarioDomain();

        when(usuarioRepository.acharPeloNomeDeUsuario(username)).thenReturn(Optional.of(entity));

        when(passwordEncoder.matches(senha, entity.getSenha())).thenReturn(true);

        when(usuarioMapper.toDomain(entity)).thenReturn(usuarioDomain);

        when(tokenService.generateAccessToken(usuarioDomain)).thenReturn(accessTokenEsperado);

        when(tokenService.generateRefreshToken(usuarioDomain)).thenReturn(refreshTokenEsperado);

        final var resultado = useCase.execute(username, senha);

        assertNotNull(resultado);

        assertEquals(accessTokenEsperado, resultado.accessToken());

        assertEquals(refreshTokenEsperado, resultado.refreshToken());

        verify(usuarioRepository).acharPeloNomeDeUsuario(username);

        verify(passwordEncoder).matches(senha, entity.getSenha());

        verify(usuarioMapper).toDomain(entity);

        verify(tokenService).generateAccessToken(usuarioDomain);

        verify(tokenService).generateRefreshToken(usuarioDomain);

        verify(refreshTokenRepository).salvar(any(), any());
    }

    @Test
    void deveSalvarRefreshTokenQuandoAutenticacaoForValida() {
        final String username = "usuario_teste";
        final String senha = "senha";

        final UsuarioEntity entity = criarUsuarioEntity();
        final Usuario usuarioDomain = criarUsuarioDomain();

        when(usuarioRepository.acharPeloNomeDeUsuario(username)).thenReturn(Optional.of(entity));

        when(passwordEncoder.matches(senha, entity.getSenha())).thenReturn(true);

        when(usuarioMapper.toDomain(entity)).thenReturn(usuarioDomain);

        when(tokenService.generateAccessToken(usuarioDomain)).thenReturn("access.token");

        when(tokenService.generateRefreshToken(usuarioDomain)).thenReturn("refresh.token");

        useCase.execute(username, senha);

        verify(refreshTokenRepository).salvar(any(), any());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        final String username = "inexistente";
        final String senha = "senha";

        when(usuarioRepository.acharPeloNomeDeUsuario(username)).thenReturn(Optional.empty());

        final RuntimeException exception = assertThrows(
                RuntimeException.class, () -> useCase.execute(username, senha)
        );

        assertEquals("Usuário não encontrado", exception.getMessage());

        verify(usuarioRepository).acharPeloNomeDeUsuario(username);

        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(tokenService);
        verifyNoInteractions(usuarioMapper);
        verifyNoInteractions(refreshTokenRepository);
    }

    @Test
    void deveLancarExcecaoQuandoSenhaInvalida() {
        final String username = "usuario_teste";
        final String senha = "senha_errada";

        final UsuarioEntity entity = criarUsuarioEntity();

        when(usuarioRepository.acharPeloNomeDeUsuario(username)).thenReturn(Optional.of(entity));

        when(passwordEncoder.matches(senha, entity.getSenha())).thenReturn(false);

        final RuntimeException exception = assertThrows(
                RuntimeException.class, () -> useCase.execute(username, senha)
        );

        assertEquals("Credenciais inválidas", exception.getMessage());

        verify(usuarioRepository).acharPeloNomeDeUsuario(username);

        verify(passwordEncoder).matches(senha, entity.getSenha());

        verifyNoInteractions(tokenService);
        verifyNoInteractions(usuarioMapper);
        verifyNoInteractions(refreshTokenRepository);
    }

    @Test
    void naoDeveGerarTokensSemValidarSenha() {
        final String username = "usuario_teste";
        final String senha = "senha_errada";

        final UsuarioEntity entity = criarUsuarioEntity();

        when(usuarioRepository.acharPeloNomeDeUsuario(username)).thenReturn(Optional.of(entity));

        when(passwordEncoder.matches(senha, entity.getSenha())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> useCase.execute(username, senha));

        verify(tokenService, never()).generateAccessToken(any());

        verify(tokenService, never()).generateRefreshToken(any());

        verifyNoInteractions(refreshTokenRepository);
    }
}