package com.ufrn.dct.bsi.touchfy.application.usecases.role;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.RoleMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.role.RoleResponse;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.role.Role;
import com.ufrn.dct.bsi.touchfy.domain.role.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ListarRolesUseCaseTest {

    private RoleRepository roleRepository;
    private RoleMapper roleMapper;
    private ListarRolesUseCase useCase;

    @BeforeEach
    void setup() {
        roleRepository = mock(RoleRepository.class);
        roleMapper = mock(RoleMapper.class);
        useCase = new ListarRolesUseCase(roleRepository, roleMapper);
    }

    private Role roleMock(final Long id, final ERole name) {
        return new Role(id, name, Set.of());
    }

    private RoleResponse responseMock(final Long id, final ERole name) {
        return new RoleResponse(id, name, List.of());
    }

    @Test
    void deveRetornarListaDeRoles() {
        final Role r1 = roleMock(1L, ERole.OUVINTE);
        final Role r2 = roleMock(2L, ERole.ARTISTA);
        final RoleResponse res1 = responseMock(1L, ERole.OUVINTE);
        final RoleResponse res2 = responseMock(2L, ERole.ARTISTA);

        when(roleRepository.listarTodos()).thenReturn(List.of(r1, r2));
        when(roleMapper.toResponse(r1)).thenReturn(res1);
        when(roleMapper.toResponse(r2)).thenReturn(res2);

        final List<RoleResponse> result = useCase.execute();

        assertEquals(2, result.size());
        assertEquals(res1, result.get(0));
        assertEquals(res2, result.get(1));
        verify(roleRepository).listarTodos();
        verify(roleMapper).toResponse(r1);
        verify(roleMapper).toResponse(r2);
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoHouverRoles() {
        when(roleRepository.listarTodos()).thenReturn(List.of());

        final List<RoleResponse> result = useCase.execute();

        assertTrue(result.isEmpty());
        verify(roleRepository).listarTodos();
        verifyNoInteractions(roleMapper);
    }
}
