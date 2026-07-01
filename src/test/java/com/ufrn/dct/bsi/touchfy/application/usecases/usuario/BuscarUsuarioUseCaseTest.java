package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BuscarUsuarioUseCaseTest {

  private UsuarioRepository usuarioRepository;
  private UsuarioMapper usuarioMapper;

  private BuscarUsuarioUseCase useCase;

  @BeforeEach
  void setup() {
    usuarioRepository = mock(UsuarioRepository.class);
    usuarioMapper = mock(UsuarioMapper.class);

    useCase = new BuscarUsuarioUseCase(usuarioRepository, usuarioMapper);
  }

  @Test
  void deveBuscarUsuarioComSucesso() {
    final UUID idUsuario = UUID.randomUUID();

    final UsuarioEntity usuarioEntity =
        UsuarioEntity.builder()
            .id(idUsuario)
            .nome("João")
            .nomeUsuario("joao123")
            .senha("123456")
            .email(new Email("joao@email.com"))
            .caminhoDaImagemDePerfil("/imagens/joao.png")
            .emailVerificado(true)
            .dataNascimento(LocalDate.of(2000, 1, 1))
            .build();

    final Usuario usuario =
        Usuario.builder()
            .id(idUsuario)
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
            List.of("OUVINTE"));

    when(usuarioRepository.acharPeloId(idUsuario)).thenReturn(Optional.of(usuarioEntity));

    when(usuarioMapper.toDomain(usuarioEntity)).thenReturn(usuario);

    when(usuarioMapper.toResponse(usuario)).thenReturn(response);

    final UsuarioResponse resultado = useCase.execute(idUsuario);

    assertNotNull(resultado);
    assertEquals(usuario.getId().toString(), resultado.id());
    assertEquals("João", resultado.nome());
    assertEquals("joao123", resultado.nomeUsuario());
    assertEquals("joao@email.com", resultado.email());
    assertEquals("2000-01-01", resultado.dataNascimento());
    assertEquals("/imagens/joao.png", resultado.fotoPerfil());
    assertEquals(List.of("OUVINTE"), resultado.roles());

    verify(usuarioRepository, times(1)).acharPeloId(idUsuario);

    verify(usuarioMapper, times(1)).toDomain(usuarioEntity);

    verify(usuarioMapper, times(1)).toResponse(usuario);
  }

  @Test
  void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
    final UUID idUsuario = UUID.randomUUID();

    when(usuarioRepository.acharPeloId(idUsuario)).thenReturn(Optional.empty());

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> useCase.execute(idUsuario));

    assertEquals("Usuário não encontrado", exception.getMessage());

    verify(usuarioRepository, times(1)).acharPeloId(idUsuario);

    verify(usuarioMapper, never()).toResponse(any());
  }
}
