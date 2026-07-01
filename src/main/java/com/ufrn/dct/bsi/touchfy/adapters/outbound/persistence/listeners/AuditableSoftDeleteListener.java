package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.listeners;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.AuditableEntity;
import jakarta.persistence.PreRemove;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Component
public class AuditableSoftDeleteListener {

  @Autowired AuditorAware<UUID> auditorAware;

  @PreRemove
  public void setDeletionMetadata(final Object entity) {
    // Quando o JPA instancia o listener diretamente, as injeções do Spring podem não ocorrer;
    // garantir o processamento das injeções para esta instância antes de usar auditorAware.
    SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

    if (entity instanceof AuditableEntity auditable) {
      auditable.setAtivo(false);
      auditable.setDeletadoEm(LocalDateTime.now());

      auditorAware.getCurrentAuditor().ifPresent(auditable::setDeletadoPor);
    }
  }
}
