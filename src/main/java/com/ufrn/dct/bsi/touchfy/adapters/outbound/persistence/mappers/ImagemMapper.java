package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers;

import com.ufrn.dct.bsi.touchfy.shared.models.Imagem;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ImagemMapper {
    @Named("mapPathToImage")
    default Imagem toDomain(final String path) {
        return new Imagem(null, path, null, null, null);
    }

    @Named("mapImageToPath")
    default String toEntity(final Imagem imagem) {
        if (imagem == null) {
            return null;
        }

        return imagem.getCaminhoDoArquivo();
    }
}
