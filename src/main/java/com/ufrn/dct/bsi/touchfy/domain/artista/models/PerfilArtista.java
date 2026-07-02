package com.ufrn.dct.bsi.touchfy.domain.artista.models;

import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PerfilArtista {
  private Usuario usuario;
  private long quantidadeSeguidores;
  private Integer posicaoRanking;
}
