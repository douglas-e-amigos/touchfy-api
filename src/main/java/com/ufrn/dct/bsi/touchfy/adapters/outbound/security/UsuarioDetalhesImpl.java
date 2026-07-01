package com.ufrn.dct.bsi.touchfy.adapters.outbound.security;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@Builder
public class UsuarioDetalhesImpl implements UserDetails {

  private final UsuarioEntity usuario;

  public UUID getId() {
    return usuario.getId();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    final Set<GrantedAuthority> authorities = new HashSet<>();
    if (usuario.getRoles() != null) {
      for (var role : usuario.getRoles()) {
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName().name()));
        if (role.getPermissions() != null) {
          for (var permission : role.getPermissions()) {
            authorities.add(new SimpleGrantedAuthority(permission.getName()));
          }
        }
      }
    }
    return authorities;
  }

  @Override
  public String getPassword() {
    return usuario.getSenha();
  }

  @Override
  public String getUsername() {
    return usuario.getNomeUsuario();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    // return usuario.getEmailVerificado();
    return true;
  }
}
