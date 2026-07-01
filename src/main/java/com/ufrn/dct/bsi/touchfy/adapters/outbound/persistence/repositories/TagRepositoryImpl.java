package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.TagEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.TagMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.TagJpaRepository;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarTagRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarTagRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Tag;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.TagRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TagRepositoryImpl implements TagRepository {
  private final AuditorAware<UUID> auditorAware;
  private final TagJpaRepository jpaRepository;
  private final TagMapper mapper;

  public TagEntity acharPeloId(final UUID id) {
    return jpaRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Tag não encontrada."));
  }

  @Override
  public void salvar(final CriarTagRequest request) {
    final var entity = TagEntity.builder().nome(request.nome()).build();
    jpaRepository.save(entity);
  }

  @Override
  public List<Tag> consultar() {
    return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
  }

  @Override
  public void atualizar(final UUID id, final AtualizarTagRequest request) {
    final var entity = acharPeloId(id);
    entity.setNome(request.nome());
    jpaRepository.save(entity);
  }

  @Override
  public void deletar(final UUID id) {
    jpaRepository
        .findById(id)
        .ifPresent(
            entity -> {
              entity.setAtivo(false);
              entity.setDeletadoEm(LocalDateTime.now());
              auditorAware.getCurrentAuditor().ifPresent(entity::setDeletadoPor);
              jpaRepository.save(entity);
            });
  }
}
