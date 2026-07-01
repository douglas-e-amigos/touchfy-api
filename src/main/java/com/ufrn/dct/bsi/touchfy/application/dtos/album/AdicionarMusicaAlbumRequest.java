package com.ufrn.dct.bsi.touchfy.application.dtos.album;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AdicionarMusicaAlbumRequest(
        @NotNull(message = "O ID da música é obrigatório.")
        UUID musicaId
) {
}
