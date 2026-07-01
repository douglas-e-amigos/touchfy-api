package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLRestriction("ativo = true")
@Table(name = "album")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class AlbumEntity extends AuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "nome", nullable = false, length = 100)
  private String nome;

  @Column(name = "descricao")
  private String descricao;

  @Column(name = "data_lancamento")
  private LocalDate dataLancamento;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "genero_musical_id")
  private GeneroMusicalEntity generoMusical;

  @Column(name = "artista_id", nullable = false)
  private UUID artistaId;

  @lombok.Builder.Default
  @OneToMany(
      mappedBy = "album",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<AlbumMusicaEntity> musicas = new ArrayList<>();
}
