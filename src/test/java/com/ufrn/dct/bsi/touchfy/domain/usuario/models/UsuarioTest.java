package com.ufrn.dct.bsi.touchfy.domain.usuario.models;

import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UsuarioTest {

    private Email emailValido() {
        return new Email("teste@email.com");
    }

    private LocalDate dataValida() {
        return LocalDate.now();
    }

    @Test
    void deveCriarUsuarioValido() {
        Usuario usuario = new Usuario(
                UUID.randomUUID(),
                "Nome",
                "username",
                "senha",
                emailValido(),
                null,
                false,
                dataValida()
        );

        assertEquals("Nome", usuario.getNome());
        assertEquals("username", usuario.getNomeUsuario());
        assertEquals("senha", usuario.getSenha());
        assertEquals("teste@email.com", usuario.getEmail().getValue());
    }

    @Test
    void deveLancarErroQuandoNomeForNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Usuario(null, null, "user", "senha", emailValido(), null, false, dataValida())
        );
    }

    @Test
    void deveLancarErroQuandoNomeUsuarioForNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Usuario(null, "Nome", null, "senha", emailValido(), null, false, dataValida())
        );
    }

    @Test
    void deveLancarErroQuandoSenhaForNula() {
        assertThrows(IllegalArgumentException.class, () ->
                new Usuario(null, "Nome", "user", null, emailValido(), null, false, dataValida())
        );
    }

    @Test
    void deveLancarErroQuandoEmailForNulo() {
        assertThrows(IllegalArgumentException.class, () ->
                new Usuario(null, "Nome", "user", "senha", null, null, false, dataValida())
        );
    }

    @Test
    void deveLancarErroQuandoDataNascimentoForNula() {
        assertThrows(IllegalArgumentException.class, () ->
                new Usuario(null, "Nome", "user", "senha", emailValido(), null, false, null)
        );
    }
}
