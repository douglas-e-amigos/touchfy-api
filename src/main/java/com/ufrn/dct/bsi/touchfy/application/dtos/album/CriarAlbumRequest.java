package com.ufrn.dct.bsi.touchfy.application.dtos.album;

import com.ufrn.dct.bsi.touchfy.domain.album.models.TipoAlbum;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.UUID;

public record CriarAlbumRequest(
    @NotBlank(message = "O nome é obrigatório.") String nome,
    String descricao,
    LocalDate dataLancamento,
    UUID generoMusicalId,
    TipoAlbum tipo) {}
