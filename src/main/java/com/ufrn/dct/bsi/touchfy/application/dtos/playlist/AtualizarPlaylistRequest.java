package com.ufrn.dct.bsi.touchfy.application.dtos.playlist;

import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Visibilidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record AtualizarPlaylistRequest(
    @NotBlank(message = "O nome é obrigatório.") String nome,
    String descricao,
    @NotNull(message = "A visibilidade é obrigatória.") Visibilidade visibilidade,
    List<UUID> usuariosConvidadosIds) {}
