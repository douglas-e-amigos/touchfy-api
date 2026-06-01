package com.ufrn.dct.bsi.touchfy.domain.musica.repositories;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarMusicaRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarMusicaRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MusicaRepository {
    void salvar(CriarMusicaRequest request, String caminhoDoArquivo);

    List<Musica> consultar();

    Optional<Musica> acharPeloId(UUID id);

    void atualizar(UUID id, AtualizarMusicaRequest request, String caminhoDoArquivo);

    void deletar(UUID id);
}