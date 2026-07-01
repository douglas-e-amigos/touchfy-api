package com.ufrn.dct.bsi.touchfy.domain.album.repositories;

import com.ufrn.dct.bsi.touchfy.application.dtos.album.AtualizarAlbumRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.CriarAlbumRequest;
import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import com.ufrn.dct.bsi.touchfy.domain.album.models.AlbumSalvo;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AlbumRepository {
    void criar(CriarAlbumRequest request, UUID artistaId);
    void atualizar(UUID id, AtualizarAlbumRequest request);
    void deletar(UUID id);
    Optional<Album> acharPeloId(UUID id);
    List<Album> listar();
    void adicionarMusica(UUID albumId, UUID musicaId, Integer ordem);
    boolean existeMusicaNoAlbum(UUID albumId, UUID musicaId);
    void salvar(UUID albumId, UUID usuarioId);
    void removerSalvo(UUID albumId, UUID usuarioId);
    boolean existeAlbumSalvo(UUID albumId, UUID usuarioId);
    List<AlbumSalvo> listarSalvos(UUID usuarioId);
}
