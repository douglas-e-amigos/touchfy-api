package com.ufrn.dct.bsi.touchfy.application.dtos.playlist;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AdicionarMusicaPlaylistRequest(
        @NotNull(message = "O ID da música é obrigatório.")
        UUID musicaId
) {
}
