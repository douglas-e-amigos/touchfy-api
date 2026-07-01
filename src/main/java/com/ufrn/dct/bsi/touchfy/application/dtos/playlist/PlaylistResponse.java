package com.ufrn.dct.bsi.touchfy.application.dtos.playlist;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Visibilidade;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PlaylistResponse(
    UUID id,
    String nome,
    String descricao,
    Visibilidade visibilidade,
    UUID donoId,
    List<Musica> musicas,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm) {}
