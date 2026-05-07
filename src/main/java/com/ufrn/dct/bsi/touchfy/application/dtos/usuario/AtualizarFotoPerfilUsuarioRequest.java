package com.ufrn.dct.bsi.touchfy.application.dtos.usuario;

import com.ufrn.dct.bsi.touchfy.application.constraints.DimensoesImagemValida;
import com.ufrn.dct.bsi.touchfy.application.constraints.ExtensaoValida;
import com.ufrn.dct.bsi.touchfy.application.constraints.TamanhoArquivoValido;
import com.ufrn.dct.bsi.touchfy.application.enums.DimensoesImagem;
import com.ufrn.dct.bsi.touchfy.application.enums.TamanhoArquivo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record AtualizarFotoPerfilUsuarioRequest(
        @NotNull
        @Schema(type = "string", format = "binary")
        @ExtensaoValida(value = {"jpg", "png", "jpeg"}, message = "A imagem precisa estar nos formatos: .jpg, .jpeg " +
                "ou .png.")
        @DimensoesImagemValida(dimensoes = DimensoesImagem.FOTO_DE_PERFIL,
                message = "A imagem deve ter entre 300x300 e 720x720 pixels.")
        @TamanhoArquivoValido(tamanhoArquivo = TamanhoArquivo.FOTO_DE_PERFIL, message = "A imagem deve pesar entre " +
                "500KB e 10MB.")
        MultipartFile foto
) {
}
