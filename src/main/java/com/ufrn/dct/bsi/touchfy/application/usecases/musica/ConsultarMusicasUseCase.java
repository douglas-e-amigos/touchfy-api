package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.MusicaMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.MusicaResponse;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ConsultarMusicasUseCase {
    private final MusicaRepository repository;
    private final MusicaMapper musicaMapper;

    public List<MusicaResponse> execute() {
        return repository.consultar()
                .stream()
                .map(musicaMapper::toResponse)
                .toList();
    }
}