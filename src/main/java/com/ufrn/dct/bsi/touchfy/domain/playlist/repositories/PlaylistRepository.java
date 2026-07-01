package com.ufrn.dct.bsi.touchfy.domain.playlist.repositories;

import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.AtualizarPlaylistRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.CriarPlaylistRequest;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Playlist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlaylistRepository {
    void criar(UUID id, CriarPlaylistRequest request, UUID donoId);
    void adicionarMusica(UUID playlistId, UUID musicaId, Integer ordem);
    void removerMusica(UUID playlistId, UUID musicaId);
    void atualizar(UUID id, AtualizarPlaylistRequest request);
    void deletar(UUID id);
    Optional<Playlist> acharPeloId(UUID id);
    List<Playlist> listarVisiveisParaUsuario(UUID usuarioId);
    List<Playlist> listarDoUsuario(UUID usuarioId);
    boolean existeMusicaNaPlaylist(UUID playlistId, UUID musicaId);
}
