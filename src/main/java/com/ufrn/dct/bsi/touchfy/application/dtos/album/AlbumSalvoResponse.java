package com.ufrn.dct.bsi.touchfy.application.dtos.album;

import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import java.util.UUID;

public record AlbumSalvoResponse(UUID id, Album album) {}
