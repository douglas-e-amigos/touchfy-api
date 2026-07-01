package com.ufrn.dct.bsi.touchfy.application.dtos.album;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.GeneroMusical;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AlbumResponse(
    UUID id,
    String nome,
    String descricao,
    LocalDate dataLancamento,
    GeneroMusical generoMusical,
    UUID artistaId,
    List<Musica> musicas,
    LocalDateTime criadoEm,
    LocalDateTime atualizadoEm) {}
