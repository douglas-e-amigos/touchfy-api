package com.ufrn.dct.bsi.touchfy.infrastructure.seed;

import com.ufrn.dct.bsi.touchfy.application.dtos.role.CreateRoleRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.role.CriarRoleUseCase;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.role.repository.RoleRepository;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(2)
@Component
@RequiredArgsConstructor
public class RoleSeeder implements ApplicationRunner {

  private final CriarRoleUseCase criarRoleUseCase;
  private final RoleRepository roleRepository;

  private static final Map<ERole, List<Long>> ROLES_PERMISSIONS =
      Map.of(
          ERole.ADMIN, List.of(1L, 2L, 3L, 4L, 5L),
          ERole.MODERADOR, List.of(1L, 2L, 3L),
          ERole.ARTISTA, List.of(1L, 2L, 3L, 4L),
          ERole.OUVINTE, List.of(2L));

  @Override
  public void run(final ApplicationArguments args) {

    for (var entry : ROLES_PERMISSIONS.entrySet()) {

      final ERole role = entry.getKey();

      if (roleRepository.existePorNome(role)) {
        continue;
      }

      criarRoleUseCase.execute(new CreateRoleRequest(role, entry.getValue()));
    }
  }
}
