package com.ufrn.dct.bsi.touchfy.application.dtos.musicas;

import com.ufrn.dct.bsi.touchfy.application.constraints.ExtensaoValida;
import com.ufrn.dct.bsi.touchfy.application.constraints.TamanhoArquivoValido;
import com.ufrn.dct.bsi.touchfy.application.enums.TamanhoArquivo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public record AtualizarMusicaRequest(
    @Size(min = 2, max = 100, message = "O nome da música deve ter entre 2 e 100 caracteres.")
        String nome,
    String letra,
    List<UUID> tagIds,
    List<UUID> generoMusicalIds,
    @Schema(type = "string", format = "binary")
        @ExtensaoValida(
            value = {"mp3"},
            message = "O arquivo da música deve estar no formato .mp3.")
        @TamanhoArquivoValido(
            tamanhoArquivo = TamanhoArquivo.MUSICA,
            message = "O arquivo da música deve pesar entre 500KB e 20MB.")
        MultipartFile arquivo) {}
