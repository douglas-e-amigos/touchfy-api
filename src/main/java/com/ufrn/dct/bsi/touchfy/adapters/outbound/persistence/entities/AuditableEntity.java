package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.listeners.AuditableSoftDeleteListener;
import jakarta.persistence.MappedSuperclass;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners({AuditingEntityListener.class, AuditableSoftDeleteListener.class})
@SuperBuilder
public class AuditableEntity {

    @CreatedBy
    @Column(name = "criado_por", updatable = false)
    private UUID criadoPor;

    @CreatedDate
    @Column(name = "criado_em", updatable = false)
    private LocalDateTime criadoEm;

    @LastModifiedBy
    @Column(name = "atualizado_por")
    private UUID atualizadoPor;

    @LastModifiedDate
    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @Column(name = "deletado_por")
    private UUID deletadoPor;

    @Column(name = "deletado_em")
    private LocalDateTime deletadoEm;

    @Builder.Default
    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
}
