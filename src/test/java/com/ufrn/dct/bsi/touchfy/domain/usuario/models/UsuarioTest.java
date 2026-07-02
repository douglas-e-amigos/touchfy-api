package com.ufrn.dct.bsi.touchfy.domain.usuario.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UsuarioTest {

  private Email emailValido() {
    return new Email("teste@email.com");
  }

  private LocalDate dataValida() {
    return LocalDate.now();
  }

  @Test
  void deveCriarUsuarioValido() {
    Usuario usuario =
        new Usuario(
            UUID.randomUUID(),
            "Nome",
            "username",
            "senha",
            emailValido(),
            null,
            false,
            dataValida(),
            null,
            Set.of());

    assertEquals("Nome", usuario.getNome());
    assertEquals("username", usuario.getNomeUsuario());
    assertEquals("senha", usuario.getSenha());
    assertEquals("teste@email.com", usuario.getEmail().getValue());
  }

  @Test
  void deveLancarErroQuandoNomeForNulo() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new Usuario(
                null,
                null,
                "user",
                "senha",
                emailValido(),
                null,
                false,
                dataValida(),
                null,
                Set.of()));
  }

  @Test
  void deveLancarErroQuandoNomeUsuarioForNulo() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new Usuario(
                null,
                "Nome",
                null,
                "senha",
                emailValido(),
                null,
                false,
                dataValida(),
                null,
                Set.of()));
  }

  @Test
  void deveLancarErroQuandoSenhaForNula() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new Usuario(
                null,
                "Nome",
                "user",
                null,
                emailValido(),
                null,
                false,
                dataValida(),
                null,
                Set.of()));
  }

  @Test
  void deveLancarErroQuandoEmailForNulo() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new Usuario(
                null, "Nome", "user", "senha", null, null, false, dataValida(), null, Set.of()));
  }

  @Test
  void deveLancarErroQuandoDataNascimentoForNula() {
    assertThrows(
        IllegalArgumentException.class,
        () ->
            new Usuario(
                null, "Nome", "user", "senha", emailValido(), null, false, null, null, Set.of()));
  }
}
