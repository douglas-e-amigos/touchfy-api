package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.security.UsuarioDetalhesImpl;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

class DesativarUsuarioUseCaseTest {

  @Test
  void deveDesativarUsuarioQuandoIdPertencerAoUsuarioLogado() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final DesativarUsuarioUseCase useCase = new DesativarUsuarioUseCase(usuarioRepository);
    final UUID usuarioId = UUID.randomUUID();
    final UsuarioEntity usuarioEntity =
        UsuarioEntity.builder().id(usuarioId).nomeUsuario("joao123").build();
    final UsuarioDetalhesImpl principal =
        UsuarioDetalhesImpl.builder().usuario(usuarioEntity).build();

    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken(principal, null, List.of()));

    useCase.execute(usuarioId);

    verify(usuarioRepository, times(1)).deletar(usuarioId);
    SecurityContextHolder.clearContext();
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoEstiverAutenticado() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final DesativarUsuarioUseCase useCase = new DesativarUsuarioUseCase(usuarioRepository);
    final UUID usuarioId = UUID.randomUUID();

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(usuarioId));

    assertEquals("Usuário não autenticado", exception.getMessage());
    verify(usuarioRepository, never()).deletar(usuarioId);
  }

  @Test
  void deveLancarExcecaoQuandoIdNaoPertencerAoUsuarioLogado() {
    final UsuarioRepository usuarioRepository = mock(UsuarioRepository.class);
    final DesativarUsuarioUseCase useCase = new DesativarUsuarioUseCase(usuarioRepository);
    final UUID usuarioId = UUID.randomUUID();
    final UsuarioEntity usuarioEntity =
        UsuarioEntity.builder().id(UUID.randomUUID()).nomeUsuario("joao123").build();
    final UsuarioDetalhesImpl principal =
        UsuarioDetalhesImpl.builder().usuario(usuarioEntity).build();

    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken(principal, null, List.of()));

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(usuarioId));

    assertEquals("Usuário não autorizado a desativar esta conta", exception.getMessage());
    verify(usuarioRepository, never()).deletar(usuarioId);
    SecurityContextHolder.clearContext();
  }
}
