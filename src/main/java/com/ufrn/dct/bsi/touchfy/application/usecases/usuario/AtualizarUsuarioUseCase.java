package com.ufrn.dct.bsi.touchfy.application.usecases.usuario;

import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.AtualizarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AtualizarUsuarioUseCase {
  private final UsuarioRepository usuarioRepository;

  public void execute(final UUID id, final AtualizarUsuarioRequest request) {
    usuarioRepository.atualizarUsuarioParcialmente(id, request);
  }
}
