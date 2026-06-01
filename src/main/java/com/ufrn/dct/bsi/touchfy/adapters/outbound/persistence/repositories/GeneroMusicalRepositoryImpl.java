package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.GeneroMusicalEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.GeneroMusicalMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.GeneroMusicalJpaRepository;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarGeneroMusicalRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarGeneroMusicalRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.GeneroMusical;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.GeneroMusicalRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class GeneroMusicalRepositoryImpl implements GeneroMusicalRepository {
    private final AuditorAware<UUID> auditorAware;
    private final GeneroMusicalJpaRepository jpaRepository;
    private final GeneroMusicalMapper mapper;

    public GeneroMusicalEntity acharPeloId(final UUID id) {
        return jpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Gênero musical não encontrado."));
    }

    @Override
    public void salvar(final CriarGeneroMusicalRequest request) {
        final var entity = GeneroMusicalEntity.builder()
                .nome(request.nome())
                .build();
        jpaRepository.save(entity);
    }

    @Override
    public List<GeneroMusical> consultar() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public void atualizar(final UUID id, final AtualizarGeneroMusicalRequest request) {
        final var entity = acharPeloId(id);
        entity.setNome(request.nome());
        jpaRepository.save(entity);
    }

    @Override
    public void deletar(final UUID id) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setAtivo(false);
            entity.setDeletadoEm(LocalDateTime.now());
            auditorAware.getCurrentAuditor().ifPresent(entity::setDeletadoPor);
            jpaRepository.save(entity);
        });
    }
}