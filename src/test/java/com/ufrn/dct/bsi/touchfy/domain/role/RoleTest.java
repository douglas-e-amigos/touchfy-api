package com.ufrn.dct.bsi.touchfy.domain.role;

import com.ufrn.dct.bsi.touchfy.domain.permission.Permission;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoleTest {

    @Test
    void deveCriarRoleComTodosOsCamposValidos() {
        final Permission permission = new Permission(1L, "READ");
        final Role role = new Role(1L, ERole.OUVINTE, Set.of(permission));

        assertEquals(1L, role.getId());
        assertEquals(ERole.OUVINTE, role.getName());
        assertNotNull(role.getPermissions());
        assertEquals(1, role.getPermissions().size());
        assertTrue(role.getPermissions().contains(permission));
    }

    @Test
    void deveCriarRoleComPermissionsVazio() {
        final Role role = new Role(1L, ERole.OUVINTE, Set.of());

        assertEquals(1L, role.getId());
        assertEquals(ERole.OUVINTE, role.getName());
        assertNotNull(role.getPermissions());
        assertTrue(role.getPermissions().isEmpty());
    }

    @Test
    void deveCriarRoleComPermissionsNulo() {
        final Role role = new Role(1L, ERole.OUVINTE, null);

        assertEquals(1L, role.getId());
        assertEquals(ERole.OUVINTE, role.getName());
        assertNull(role.getPermissions());
    }
}
