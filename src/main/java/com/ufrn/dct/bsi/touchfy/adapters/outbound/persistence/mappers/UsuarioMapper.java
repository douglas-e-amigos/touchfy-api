package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.application.dtos.usuario.CriarUsuarioRequest;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ImagemMapper.class, EmailMapper.class})
public interface UsuarioMapper {
    @Mapping(source = "caminhoDaImagemDePerfil", target = "imagem", qualifiedByName = "mapPathToImage")
    Usuario toDomain(UsuarioEntity usuarioEntity);

    @Mapping(source = "imagem", target = "caminhoDaImagemDePerfil", qualifiedByName = "mapImageToPath")
    UsuarioEntity toEntity(Usuario usuario);

    @Mapping(target = "imagem",  ignore = true)
    @Mapping(source = "email", target = "email", qualifiedByName = "mapValueToEmail")
    Usuario toDomain(CriarUsuarioRequest request);
}
