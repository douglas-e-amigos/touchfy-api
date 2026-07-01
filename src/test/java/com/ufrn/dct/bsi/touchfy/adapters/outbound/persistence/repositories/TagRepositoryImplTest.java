package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.TagEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.TagMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.TagJpaRepository;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarTagRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarTagRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Tag;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.AuditorAware;

class TagRepositoryImplTest {

  private AuditorAware<UUID> auditorAware;
  private TagJpaRepository jpaRepository;
  private TagMapper mapper;

  private TagRepositoryImpl repository;

  @SuppressWarnings("unchecked")
  private void initRepository() {
    auditorAware = (AuditorAware<UUID>) mock(AuditorAware.class);
    jpaRepository = mock(TagJpaRepository.class);
    mapper = mock(TagMapper.class);

    repository = new TagRepositoryImpl(auditorAware, jpaRepository, mapper);
  }

  @Test
  void deveSalvarTag() {
    initRepository();
    final CriarTagRequest request = new CriarTagRequest("rock");

    repository.salvar(request);

    final ArgumentCaptor<TagEntity> captor = ArgumentCaptor.forClass(TagEntity.class);
    verify(jpaRepository).save(captor.capture());
    assertEquals("rock", captor.getValue().getNome());
  }

  @Test
  void deveConsultarTagsMapeandoEntidadesParaDominio() {
    initRepository();
    final TagEntity entity = TagEntity.builder().id(UUID.randomUUID()).nome("rock").build();
    final Tag tag = Tag.builder().id(entity.getId()).nome(entity.getNome()).build();

    when(jpaRepository.findAll()).thenReturn(List.of(entity));
    when(mapper.toDomain(entity)).thenReturn(tag);

    final List<Tag> response = repository.consultar();

    assertEquals(1, response.size());
    assertEquals(tag, response.get(0));
    verify(jpaRepository, times(1)).findAll();
    verify(mapper, times(1)).toDomain(entity);
  }

  @Test
  void deveAtualizarTagQuandoEncontrada() {
    initRepository();
    final UUID id = UUID.randomUUID();
    final AtualizarTagRequest request = new AtualizarTagRequest("metal");
    final TagEntity entity = TagEntity.builder().id(id).nome("rock").build();

    when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

    repository.atualizar(id, request);

    assertEquals("metal", entity.getNome());
    verify(jpaRepository).findById(id);
    verify(jpaRepository).save(entity);
  }

  @Test
  void deveLancarExcecaoQuandoTagNaoForEncontradaPeloId() {
    initRepository();
    final UUID id = UUID.randomUUID();

    when(jpaRepository.findById(id)).thenReturn(Optional.empty());

    final RuntimeException exception =
        assertThrows(RuntimeException.class, () -> repository.acharPeloId(id));

    assertEquals("Tag não encontrada.", exception.getMessage());
    verify(jpaRepository).findById(id);
  }

  @Test
  void deveDeletarTagComSoftDeleteQuandoEncontrada() {
    initRepository();
    final UUID id = UUID.randomUUID();
    final UUID auditorId = UUID.randomUUID();
    final TagEntity entity = TagEntity.builder().id(id).nome("rock").ativo(true).build();

    when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
    when(auditorAware.getCurrentAuditor()).thenReturn(Optional.of(auditorId));

    repository.deletar(id);

    assertFalse(entity.getAtivo());
    assertEquals(auditorId, entity.getDeletadoPor());
    assertNotNull(entity.getDeletadoEm());
    verify(jpaRepository).findById(id);
    verify(auditorAware).getCurrentAuditor();
    verify(jpaRepository).save(entity);
  }

  @Test
  void naoDeveSalvarQuandoTagNaoForEncontradaAoDeletar() {
    initRepository();
    final UUID id = UUID.randomUUID();

    when(jpaRepository.findById(id)).thenReturn(Optional.empty());

    repository.deletar(id);

    verify(jpaRepository).findById(id);
    verify(jpaRepository, never()).save(any());
  }

  @Test
  void deveRetornarTagQuandoAcharPeloIdEncontrarRegistro() {
    initRepository();
    final UUID id = UUID.randomUUID();
    final TagEntity entity = TagEntity.builder().id(id).nome("rock").build();

    when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

    final TagEntity response = repository.acharPeloId(id);

    assertTrue(response.getId().equals(id));
    assertEquals("rock", response.getNome());
    verify(jpaRepository).findById(id);
  }
}
