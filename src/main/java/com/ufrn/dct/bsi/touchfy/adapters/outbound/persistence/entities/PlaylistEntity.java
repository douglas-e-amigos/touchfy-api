package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities;

import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Visibilidade;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLRestriction("ativo = true")
@Table(name = "playlist")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class PlaylistEntity extends AuditableEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "nome", nullable = false, length = 100)
  private String nome;

  @Column(name = "descricao")
  private String descricao;

  @Enumerated(EnumType.STRING)
  @Column(name = "visibilidade", nullable = false, length = 20)
  private Visibilidade visibilidade;

  @Column(name = "dono_id", nullable = false)
  private UUID donoId;

  @lombok.Builder.Default
  @OneToMany(
      mappedBy = "playlist",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  private List<PlaylistMusicaEntity> musicas = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "playlist_usuarios_convidados",
      joinColumns = @JoinColumn(name = "playlist_id"),
      inverseJoinColumns = @JoinColumn(name = "usuario_id"))
  private Set<UsuarioEntity> usuariosConvidados;
}
