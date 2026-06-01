package com.ufrn.dct.bsi.touchfy.domain.musica.repositories;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarTagRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarTagRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Tag;

import java.util.List;
import java.util.UUID;

public interface TagRepository {
    void salvar(CriarTagRequest request);

    List<Tag> consultar();

    void atualizar(UUID id, AtualizarTagRequest request);

    void deletar(UUID id);
}
