package com.ufrn.dct.bsi.touchfy.infrastructure.security;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.security.UsuarioDetalhesImpl;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityAuditorAwareImpl implements AuditorAware<UUID> {

  @Override
  public Optional<UUID> getCurrentAuditor() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || !authentication.isAuthenticated()
        || "anonymousUser".equals(authentication.getPrincipal())) {
      return Optional.empty();
    }

    final Object principal = authentication.getPrincipal();

    if (principal instanceof UsuarioDetalhesImpl usuarioDetalhes) {
      return Optional.ofNullable(usuarioDetalhes.getId());
    }

    return Optional.empty();
  }
}
