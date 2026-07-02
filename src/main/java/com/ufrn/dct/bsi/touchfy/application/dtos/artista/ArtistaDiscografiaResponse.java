package com.ufrn.dct.bsi.touchfy.application.dtos.artista;

import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumResponse;
import java.util.List;
import lombok.Builder;

@Builder
public record ArtistaDiscografiaResponse(
    List<AlbumResponse> albuns,
    List<AlbumResponse> singles,
    List<AlbumResponse> eps,
    List<AlbumResponse> compilacoes) {}
