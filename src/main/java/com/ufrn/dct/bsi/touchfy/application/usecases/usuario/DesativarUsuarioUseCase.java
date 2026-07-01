package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.security.UsuarioDetalhesImpl;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.AcessoNegadoException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.NaoAutenticadoException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DesativarUsuarioUseCase {
  private final UsuarioRepository usuarioRepository;

  public void execute(final UUID idUsuario) {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null
        || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getPrincipal())) {
      throw new NaoAutenticadoException("Usuário não autenticado");
    }

    final Object principal = authentication.getPrincipal();

    if (!(principal instanceof UsuarioDetalhesImpl usuarioDetalhes)) {
      throw new NaoAutenticadoException("Usuário não autenticado");
    }

    if (!usuarioDetalhes.getId().equals(idUsuario)) {
      throw new AcessoNegadoException("Usuário não autorizado a desativar esta conta");
    }

    usuarioRepository.deletar(idUsuario);
  }
}
