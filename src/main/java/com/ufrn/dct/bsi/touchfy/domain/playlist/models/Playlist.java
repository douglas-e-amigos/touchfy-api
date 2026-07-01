package com.ufrn.dct.bsi.touchfy.domain.playlist.models;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Playlist {
  private UUID id;
  private String nome;
  private String descricao;
  private Visibilidade visibilidade;
  private UUID donoId;
  private List<Musica> musicas;
  private List<UUID> usuariosConvidadosIds;

  public Playlist(
      final UUID id,
      final String nome,
      final String descricao,
      final Visibilidade visibilidade,
      final UUID donoId,
      final List<Musica> musicas,
      final List<UUID> usuariosConvidadosIds) {
    if (id == null) {
      throw new IllegalArgumentException("ID é obrigatório");
    }
    if (nome == null || nome.isBlank()) {
      throw new IllegalArgumentException("Nome é obrigatório");
    }
    if (visibilidade == null) {
      throw new IllegalArgumentException("Visibilidade é obrigatória");
    }
    if (donoId == null) {
      throw new IllegalArgumentException("Dono é obrigatório");
    }

    this.id = id;
    this.nome = nome;
    this.descricao = descricao;
    this.visibilidade = visibilidade;
    this.donoId = donoId;
    this.musicas = musicas == null ? List.of() : musicas;
    this.usuariosConvidadosIds = usuariosConvidadosIds == null ? List.of() : usuariosConvidadosIds;
  }
}
