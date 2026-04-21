package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers;

import com.ufrn.dct.bsi.touchfy.shared.models.Imagem;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ImagemMapper {
    @Named("mapPathToImage")
    default Imagem toDomain(String path) {
        return Imagem.builder()
                .caminhoDoArquivo(path)
                .nome(null)
                .tamanhoEmBytes(null)
                .textoAlternativo(null)
                .extensao(null)
                .build();
    }

    @Named("mapImageToPath")
    default String toEntity(Imagem imagem) {
        if (imagem == null) {
            return null;
        }

        return imagem.getCaminhoDoArquivo();
    }
}
