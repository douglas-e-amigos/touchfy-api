package com.ufrn.dct.bsi.touchfy.infrastructure.seed;

import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.CriarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.application.usecases.usuario.CriarUsuarioUseCase;
import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(1)
@Profile("dev")
@Component
@RequiredArgsConstructor
public class UsuariosTesteSeeder implements ApplicationRunner {

  private final CriarUsuarioUseCase criarUsuarioUseCase;
  private final UsuarioRepository repository;

  @Override
  public void run(final ApplicationArguments args) {
    criarUsuarioSeNaoExistir(
        new CriarUsuarioRequest(
            "Joãozinho pé de barro",
            "joaozinho",
            "senhaforte",
            "senhaforte",
            "joaozinho@ouvinte.com",
            LocalDate.of(2000, 1, 1),
            ERole.OUVINTE));

    criarUsuarioSeNaoExistir(
        new CriarUsuarioRequest(
            "Anitta Pennywise",
            "anitta",
            "senhaforte",
            "senhaforte",
            "pennywise@artista.com",
            LocalDate.of(2000, 1, 1),
            ERole.ARTISTA));

    criarUsuarioSeNaoExistir(
        new CriarUsuarioRequest(
            "Akemi Mosiah",
            "akemi",
            "senhaforte",
            "senhaforte",
            "akemi@admin.com",
            LocalDate.of(2000, 1, 1),
            ERole.ADMIN));
  }

  private void criarUsuarioSeNaoExistir(final CriarUsuarioRequest request) {
    if (repository.acharPeloNomeDeUsuario(request.nomeUsuario()).isEmpty()) {
      criarUsuarioUseCase.execute(request);
    }
  }
}
