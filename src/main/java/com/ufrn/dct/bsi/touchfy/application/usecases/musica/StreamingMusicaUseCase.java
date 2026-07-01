package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import java.util.Arrays;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.StreamingMusicaResponse;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.BuscarArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import com.ufrn.dct.bsi.touchfy.shared.dtos.ArquivoRecuperadoResponse;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class StreamingMusicaUseCase {
    private final MusicaRepository musicaRepository;
    private final BuscarArquivoUseCase buscarArquivoUseCase;

    public StreamingMusicaResponse execute(final UUID id, final String rangeHeader) {
        final Musica musica = musicaRepository.acharPeloId(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Música não encontrada."));

        final ArquivoRecuperadoResponse arquivo = buscarArquivoUseCase.execute(musica.getCaminhoDoArquivo());
        final long tamanhoTotal = arquivo.conteudo().length;

        if (tamanhoTotal == 0) {
            return new StreamingMusicaResponse(
                    arquivo.nome(),
                    arquivo.contentType(),
                    arquivo.conteudo(),
                    0,
                    0,
                    0,
                    false);
        }

        final Range intervalo = parseRange(rangeHeader, tamanhoTotal);

        return new StreamingMusicaResponse(
                arquivo.nome(),
                arquivo.contentType(),
                Arrays.copyOfRange(
                        arquivo.conteudo(),
                        (int) intervalo.inicio(),
                        (int) intervalo.fim() + 1),
                intervalo.inicio(),
                intervalo.fim(),
                tamanhoTotal,
                intervalo.parcial());
    }

    private Range parseRange(final String rangeHeader, final long tamanhoTotal) {
        if (rangeHeader == null || rangeHeader.isBlank()) {
            return new Range(0, tamanhoTotal - 1, false);
        }

        if (!rangeHeader.startsWith("bytes=")) {
            throw new ResponseStatusException(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE, "Range inválido.");
        }

        final String faixa = rangeHeader.substring("bytes=".length()).trim();

        if (faixa.isBlank() || faixa.contains(",")) {
            throw new ResponseStatusException(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE, "Range inválido.");
        }

        final String[] limites = faixa.split("-", -1);
        if (limites.length != 2) {
            throw new ResponseStatusException(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE, "Range inválido.");
        }

        try {
            final long inicio;
            final long fim;

            if (limites[0].isBlank()) {
                final long sufixo = Long.parseLong(limites[1]);
                if (sufixo <= 0) {
                    throw new ResponseStatusException(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE, "Range inválido.");
                }

                inicio = Math.max(tamanhoTotal - sufixo, 0);
                fim = tamanhoTotal - 1;
            } else {
                inicio = Long.parseLong(limites[0]);
                fim = limites[1].isBlank() ? tamanhoTotal - 1 : Math.min(Long.parseLong(limites[1]), tamanhoTotal - 1);
            }

            if (inicio < 0 || inicio >= tamanhoTotal || fim < inicio) {
                throw new ResponseStatusException(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE, "Range inválido.");
            }

            return new Range(inicio, fim, true);
        } catch (final NumberFormatException exception) {
            throw new ResponseStatusException(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE, "Range inválido.", exception);
        }
    }

    private record Range(long inicio, long fim, boolean parcial) {
    }
}