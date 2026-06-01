package com.ufrn.dct.bsi.touchfy.domain.musica.repositories;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarGeneroMusicalRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarGeneroMusicalRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.GeneroMusical;

import java.util.List;
import java.util.UUID;

public interface GeneroMusicalRepository {
    void salvar(CriarGeneroMusicalRequest request);

    List<GeneroMusical> consultar();

    void atualizar(UUID id, AtualizarGeneroMusicalRequest request);

    void deletar(UUID id);
}