package com.ufrn.dct.bsi.touchfy.adapters.outbound.security;

import static org.junit.jupiter.api.Assertions.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.PermissionEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.RoleEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UsuarioDetalhesImplTest {

  private UsuarioEntity criarUsuario() {
    return UsuarioEntity.builder()
        .id(UUID.randomUUID())
        .nome("Nome Teste")
        .nomeUsuario("usuario_teste")
        .senha("senha_hash")
        .email(new Email("teste@email.com"))
        .dataNascimento(LocalDate.now())
        .build();
  }

  @Test
  void deveRetornarUsernameCorretamente() {
    final UsuarioEntity usuario = criarUsuario();
    final UsuarioDetalhesImpl detalhes = UsuarioDetalhesImpl.builder().usuario(usuario).build();
    assertEquals("usuario_teste", detalhes.getUsername());
  }

  @Test
  void deveRetornarSenhaCorretamente() {
    final UsuarioEntity usuario = criarUsuario();
    final UsuarioDetalhesImpl detalhes = UsuarioDetalhesImpl.builder().usuario(usuario).build();
    assertEquals("senha_hash", detalhes.getPassword());
  }

  @Test
  void deveRetornarAuthoritiesVazia() {
    final UsuarioEntity usuario = criarUsuario();
    final UsuarioDetalhesImpl detalhes = UsuarioDetalhesImpl.builder().usuario(usuario).build();
    assertTrue(detalhes.getAuthorities().isEmpty());
  }

  @Test
  void deveRetornarContaValida() {
    final UsuarioEntity usuario = criarUsuario();
    final UsuarioDetalhesImpl detalhes = UsuarioDetalhesImpl.builder().usuario(usuario).build();
    assertTrue(detalhes.isAccountNonExpired());
    assertTrue(detalhes.isAccountNonLocked());
    assertTrue(detalhes.isCredentialsNonExpired());
    assertTrue(detalhes.isEnabled());
  }

  @Test
  void deveRetornarAuthoritiesComRolesEPermissoes() {
    final PermissionEntity perm1 = new PermissionEntity();
    perm1.setId(1L);
    perm1.setName("user:read");
    final PermissionEntity perm2 = new PermissionEntity();
    perm2.setId(2L);
    perm2.setName("user:write");
    final RoleEntity role = new RoleEntity();
    role.setId(1L);
    role.setName(ERole.ADMIN);
    role.setPermissions(Set.of(perm1, perm2));
    final UsuarioEntity usuario = criarUsuario();
    usuario.setRoles(Set.of(role));

    final UsuarioDetalhesImpl detalhes = UsuarioDetalhesImpl.builder().usuario(usuario).build();
    final var authorities = detalhes.getAuthorities();

    assertEquals(3, authorities.size());
    assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("user:read")));
    assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("user:write")));
  }

  @Test
  void deveRetornarAuthoritiesComRolesQuandoPermissionsNulas() {
    final RoleEntity role = new RoleEntity();
    role.setId(1L);
    role.setName(ERole.OUVINTE);
    role.setPermissions(null);
    final UsuarioEntity usuario = criarUsuario();
    usuario.setRoles(Set.of(role));

    final UsuarioDetalhesImpl detalhes = UsuarioDetalhesImpl.builder().usuario(usuario).build();
    final var authorities = detalhes.getAuthorities();

    assertEquals(1, authorities.size());
    assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_OUVINTE")));
  }
}
