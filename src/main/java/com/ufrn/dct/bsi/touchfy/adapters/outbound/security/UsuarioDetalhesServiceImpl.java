package com.ufrn.dct.bsi.touchfy.adapters.outbound.security;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioDetalhesServiceImpl implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Override
    public UserDetails loadUserByUsername(final String username) {
        final UsuarioEntity usuarioEntity = usuarioRepository.acharPeloNomeDeUsuario(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return UsuarioDetalhesImpl.builder().usuario(usuarioEntity).build();
    }
}
