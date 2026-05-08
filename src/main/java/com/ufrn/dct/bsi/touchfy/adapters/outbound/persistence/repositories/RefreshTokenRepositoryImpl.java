package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.RefreshTokenEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.UsuarioMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.RefreshTokenJpaRepository;
import com.ufrn.dct.bsi.touchfy.domain.usuario.models.Usuario;
import com.ufrn.dct.bsi.touchfy.domain.usuario.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository jpaRepository;
    private final UsuarioMapper usuarioMapper;
    private final long refreshExpiration;

    public RefreshTokenRepositoryImpl(
            final RefreshTokenJpaRepository jpaRepository,
            final UsuarioMapper usuarioMapper,
            @Value("${jwt.refresh.expiration}") final long refreshExpiration
    ) {
        this.jpaRepository = jpaRepository;
        this.usuarioMapper = usuarioMapper;
        this.refreshExpiration = refreshExpiration;
    }

    @Override
    public Optional<RefreshTokenEntity> acharPeloToken(final String token) {
        return jpaRepository.findByToken(token);
    }

    @Override
    public void salvar(final Usuario usuario, final String token) {
        final var usuarioEntity = usuarioMapper.toEntity(usuario);
        final var refreshTokenEntity = RefreshTokenEntity.builder()
                .token(token)
                .usuario(usuarioEntity)
                .expiracao(Instant.now().plusSeconds(refreshExpiration / 1000))
                .revogado(false)
                .build();

        jpaRepository.save(refreshTokenEntity);
    }

    @Override
    public void revogar(final String token) {
        final var entity = this.acharPeloToken(token)
                .orElseThrow(() -> new RuntimeException("Token não encontrado"));
        entity.setRevogado(true);
        jpaRepository.save(entity);
    }
}
