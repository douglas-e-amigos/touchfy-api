package com.ufrn.dct.bsi.touchfy.domain.permission;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class PermissionTest {

  @Test
  void deveCriarPermissionComIdENomeValidos() {
    final Permission permission = new Permission(1L, "READ_PERMISSIONS");

    assertEquals(1L, permission.getId());
    assertEquals("READ_PERMISSIONS", permission.getName());
  }

  @Test
  void deveCriarPermissionComNomeNulo() {
    final Permission permission = new Permission(1L, null);

    assertEquals(1L, permission.getId());
    assertNull(permission.getName());
  }
}
