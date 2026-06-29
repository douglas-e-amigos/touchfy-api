package com.ufrn.dct.bsi.touchfy.application.dtos.musicas;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.GeneroMusical;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Tag;

import java.util.List;
import java.util.UUID;

public record MusicaResponse(
        UUID id,
        String nome,
        String caminhoDoArquivo,
        String letra,
        UUID artistaId,
        String artistaNome,
        String artistaNomeUsuario,
        List<Tag> tags,
        List<GeneroMusical> generosMusicais
) {
}
