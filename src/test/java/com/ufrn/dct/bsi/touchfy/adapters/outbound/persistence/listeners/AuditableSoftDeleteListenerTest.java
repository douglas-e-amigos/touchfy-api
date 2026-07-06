package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.listeners;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AuditableEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.AuditorAware;

class AuditableSoftDeleteListenerTest {

  @Test
  void deveMarcarEntityComoInativoEDeletado() {
    final UUID usuarioId = UUID.randomUUID();
    final AuditorAware<UUID> auditorAware = () -> Optional.of(usuarioId);
    final AuditableSoftDeleteListener listener = new AuditableSoftDeleteListener();
    listener.auditorAware = auditorAware;
    final AuditableEntity entity =
        UsuarioEntity.builder()
            .id(UUID.randomUUID())
            .nome("Teste")
            .nomeUsuario("teste")
            .senha("senha")
            .email(new com.ufrn.dct.bsi.touchfy.shared.models.Email("teste@test.com"))
            .dataNascimento(LocalDate.of(2000, 1, 1))
            .build();

    assertTrue(entity.getAtivo());
    assertNull(entity.getDeletadoEm());
    assertNull(entity.getDeletadoPor());

    listener.setDeletionMetadata(entity);

    assertFalse(entity.getAtivo());
    assertNotNull(entity.getDeletadoEm());
    assertEquals(usuarioId, entity.getDeletadoPor());
  }

  @Test
  void deveIgnorarEntityNaoAuditavel() {
    final AuditableSoftDeleteListener listener = new AuditableSoftDeleteListener();
    assertDoesNotThrow(() -> listener.setDeletionMetadata("nao sou auditavel"));
  }

  @Test
  void deveMarcarSemDeletadoPorQuandoAuditorVazio() {
    final AuditorAware<UUID> auditorAware = () -> Optional.empty();
    final AuditableSoftDeleteListener listener = new AuditableSoftDeleteListener();
    listener.auditorAware = auditorAware;
    final AuditableEntity entity =
        UsuarioEntity.builder()
            .id(UUID.randomUUID())
            .nome("Teste")
            .nomeUsuario("teste")
            .senha("senha")
            .email(new com.ufrn.dct.bsi.touchfy.shared.models.Email("teste@test.com"))
            .dataNascimento(LocalDate.of(2000, 1, 1))
            .build();

    listener.setDeletionMetadata(entity);

    assertFalse(entity.getAtivo());
    assertNotNull(entity.getDeletadoEm());
    assertNull(entity.getDeletadoPor());
  }
}
