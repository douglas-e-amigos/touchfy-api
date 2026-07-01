package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.UsuarioResponse;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import com.ufrn.dct.bsi.touchfy.shared.models.Imagem;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

class BuscarUsuarioLogadoUseCaseTest {

  private UsuarioRepository usuarioRepository;
  private UsuarioMapper usuarioMapper;

  private BuscarUsuarioLogadoUseCase useCase;

  private void criarUseCase() {
    usuarioRepository = mock(UsuarioRepository.class);
    usuarioMapper = mock(UsuarioMapper.class);
    useCase = new BuscarUsuarioLogadoUseCase(usuarioRepository, usuarioMapper);
  }

  @Test
  void deveBuscarUsuarioLogadoComSucesso() {
    criarUseCase();
    final Usuario usuario =
        Usuario.builder()
            .id(UUID.randomUUID())
            .nome("João")
            .nomeUsuario("joao123")
            .senha("123456")
            .email(new Email("joao@email.com"))
            .imagem(new Imagem("imagem.com", "/imagens/joao.png", null, null, null))
            .emailVerificado(true)
            .dataNascimento(LocalDate.of(2000, 1, 1))
            .build();

    final UsuarioResponse response =
        new UsuarioResponse(
            usuario.getId().toString(),
            "João",
            "joao123",
            "joao@email.com",
            "2000-01-01",
            "/imagens/joao.png",
            List.of("ARTISTA"));

    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("joao123", null, List.of()));

    when(usuarioRepository.buscarPorNomeUsuario("joao123")).thenReturn(Optional.of(usuario));
    when(usuarioMapper.toResponse(usuario)).thenReturn(response);

    final UsuarioResponse resultado = useCase.execute();

    assertEquals(usuario.getId().toString(), resultado.id());
    assertEquals("João", resultado.nome());
    assertEquals("joao123", resultado.nomeUsuario());
    assertEquals("joao@email.com", resultado.email());
    assertEquals("2000-01-01", resultado.dataNascimento());
    assertEquals("/imagens/joao.png", resultado.fotoPerfil());
    assertEquals(List.of("ARTISTA"), resultado.roles());

    verify(usuarioRepository, times(1)).buscarPorNomeUsuario("joao123");
    verify(usuarioMapper, times(1)).toResponse(usuario);
    SecurityContextHolder.clearContext();
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoEstiverAutenticado() {
    criarUseCase();
    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute());

    assertEquals("Usuário não autenticado", exception.getMessage());
    verify(usuarioRepository, never())
        .buscarPorNomeUsuario(org.mockito.ArgumentMatchers.anyString());
    verify(usuarioMapper, never()).toResponse(org.mockito.ArgumentMatchers.any());
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioLogadoNaoForEncontrado() {
    criarUseCase();
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken("joao123", null, List.of()));

    when(usuarioRepository.buscarPorNomeUsuario("joao123")).thenReturn(Optional.empty());

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute());

    assertEquals("Usuário não encontrado", exception.getMessage());
    verify(usuarioRepository, times(1)).buscarPorNomeUsuario("joao123");
    verify(usuarioMapper, never()).toResponse(org.mockito.ArgumentMatchers.any());
    assertTrue(SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
    SecurityContextHolder.clearContext();
  }
}
