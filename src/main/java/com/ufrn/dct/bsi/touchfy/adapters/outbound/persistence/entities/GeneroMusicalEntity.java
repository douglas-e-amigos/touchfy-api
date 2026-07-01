package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLRestriction("ativo = true")
@Table(name = "genero_musical")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class GeneroMusicalEntity extends AuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "nome", nullable = false, length = 100)
  private String nome;
}
