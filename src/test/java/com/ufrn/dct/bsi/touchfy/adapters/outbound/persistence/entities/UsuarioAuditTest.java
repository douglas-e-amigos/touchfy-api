package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.listeners.AuditableSoftDeleteListener;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.EmailMapperImpl;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.ImagemMapperImpl;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapperImpl;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.UsuarioRepositoryImpl;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.AtualizarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.config.AuditTestConfig;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=none",
        "spring.flyway.enabled=true"
})
@Import({
        AuditTestConfig.class,
        UsuarioRepositoryImpl.class,
        UsuarioMapperImpl.class,
        ImagemMapperImpl.class,
    EmailMapperImpl.class,
    AuditableSoftDeleteListener.class
})
public class UsuarioAuditTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Usuario criarUsuarioExemplo() {
        return Usuario.builder()
                .nome("user test")
                .nomeUsuario("test" + UUID.randomUUID())
                .senha(UUID.randomUUID().toString())
                .email(new Email("teste@ufrn.com"))
                .dataNascimento(LocalDate.of(1980, 1, 1))
                .build();
    }

    @Test
    @WithMockUser("sistema")
    void DevePreencherCamposDeCriacaoUsuarioAudit() {
        Usuario usuario = criarUsuarioExemplo();
        UsuarioEntity salvo = usuarioRepository.salvar(usuario);
        entityManager.flush();

        assertThat(salvo.getCriadoPor()).isNotNull();
        assertThat(salvo.getCriadoPor()).isEqualTo(
                UUID.nameUUIDFromBytes("sistema".getBytes())
        );
        assertThat(salvo.getCriadoEm()).isNotNull();
    }

    @Test
    @WithMockUser("sistema")
    void DevePreencherCamposDeAlteracaoUsuarioAudit() throws InterruptedException {
        Usuario usuario = criarUsuarioExemplo();
        UsuarioEntity salvo = usuarioRepository.salvar(usuario);
        entityManager.flush();

        LocalDateTime dataCriacao = salvo.getCriadoEm();
        UUID id = salvo.getId();

        Thread.sleep(Duration.ofSeconds(1).toMillis());

        AtualizarUsuarioRequest request = AtualizarUsuarioRequest.builder()
                .nome("Zelezin")
                .nomeUsuario("zelezin_matador_de_porco")
                .dataNascimento(LocalDate.of(1985, 5, 12))
                .build();

        usuarioRepository.atualizarUsuarioParcialmente(id, request);
        entityManager.flush();

        UsuarioEntity usuarioAtualizado = usuarioRepository.acharPeloId(id).get();

        assertThat(usuarioAtualizado.getAtualizadoPor()).isNotNull();
        assertThat(usuarioAtualizado.getAtualizadoPor()).isEqualTo(
                UUID.nameUUIDFromBytes("sistema".getBytes())
        );
        assertThat(usuarioAtualizado.getAtualizadoEm()).isAfter(dataCriacao);
        assertThat(usuarioAtualizado.getNome()).isEqualTo("Zelezin");
    }

    @Test
    @WithMockUser("admin")
    void DeveFazerSoftDeleteComAuditoriaCompleta() {
        Usuario usuario = criarUsuarioExemplo();
        UsuarioEntity salvo = usuarioRepository.salvar(usuario);
        entityManager.flush();
        UUID usuarioId = salvo.getId();

        usuarioRepository.deletar(usuarioId);
        entityManager.flush();

        UsuarioEntity deletado = entityManager.find(UsuarioEntity.class, usuarioId);

        assertThat(deletado.getAtivo()).isFalse();
        assertThat(deletado.getDeletadoEm()).isNotNull();
        assertThat(deletado.getDeletadoPor()).isNotNull();
        assertThat(deletado.getDeletadoPor()).isEqualTo(
                UUID.nameUUIDFromBytes("admin".getBytes())
        );
    }

    @Test
    void DeveFazerSoftDeleteSemUsuarioAutenticado() {
        Usuario usuario = criarUsuarioExemplo();
        UsuarioEntity salvo = usuarioRepository.salvar(usuario);
        entityManager.flush();
        UUID usuarioId = salvo.getId();

        usuarioRepository.deletar(usuarioId);
        entityManager.flush();

        UsuarioEntity deletado = entityManager.find(UsuarioEntity.class, usuarioId);

        assertThat(deletado.getAtivo()).isFalse();
        assertThat(deletado.getDeletadoEm()).isNotNull();
        assertThat(deletado.getDeletadoPor()).isNull();
    }

    @Test
    @WithMockUser("admin")
    void NaoDeveretornarUsuarioDeletado() {
        Usuario usuario = criarUsuarioExemplo();
        UsuarioEntity salvo = usuarioRepository.salvar(usuario);
        entityManager.flush();
        UUID usuarioId = salvo.getId();
        
        usuarioRepository.deletar(usuarioId);
        entityManager.flush();
        entityManager.clear();

        Optional<UsuarioEntity> encontrado = usuarioRepository.acharPeloId(usuarioId);

        // Deve retornar vazio pois o usuário foi deletado (soft delete)
        assertThat(encontrado).isEmpty();
    }

    @Test
    @WithMockUser("admin")
    void DevePreencherTimestampDeDeletacao() throws InterruptedException {
        Usuario usuario = criarUsuarioExemplo();
        UsuarioEntity salvo = usuarioRepository.salvar(usuario);
        entityManager.flush();
        UUID usuarioId = salvo.getId();
        LocalDateTime antes = LocalDateTime.now();

        Thread.sleep(100);
        usuarioRepository.deletar(usuarioId);
        entityManager.flush();
        LocalDateTime depois = LocalDateTime.now();

        UsuarioEntity deletado = entityManager.find(UsuarioEntity.class, usuarioId);

        assertThat(deletado.getDeletadoEm()).isNotNull();
        assertThat(deletado.getDeletadoEm()).isAfter(antes);
        assertThat(deletado.getDeletadoEm()).isBefore(depois);
    }
}