package com.ufrn.dct.bsi.touchfy.application.usecases.musica;

import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarMusicaRequest;
import com.ufrn.dct.bsi.touchfy.application.enums.DiretorioStorage;
import com.ufrn.dct.bsi.touchfy.application.usecases.arquivo.UploadArquivoUseCase;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CriarMusicaUseCase {
    private final MusicaRepository repository;
    private final UploadArquivoUseCase uploadArquivoUseCase;

    public void execute(final CriarMusicaRequest request) {
        if (request == null || request.nome() == null || request.nome().isBlank()) {
            throw new IllegalArgumentException("Os dados da música são obrigatórios.");
        }

        final String diretorio = String.format(DiretorioStorage.MUSICAS.getDiretorio(), UUID.randomUUID());
        final var armazenamento = uploadArquivoUseCase.execute(request.arquivo(), diretorio);
        repository.salvar(request, armazenamento.caminhoDoArquivo());
    }
}