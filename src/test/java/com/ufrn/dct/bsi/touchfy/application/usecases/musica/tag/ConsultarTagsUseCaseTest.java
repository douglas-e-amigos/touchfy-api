package com.ufrn.dct.bsi.touchfy.application.usecases.musica.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ufrn.dct.bsi.touchfy.domain.musica.models.Tag;
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.TagRepository;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ConsultarTagsUseCaseTest {

  @Test
  void deveConsultarTags() {
    final TagRepository repository = mock(TagRepository.class);
    final ConsultarTagsUseCase useCase = new ConsultarTagsUseCase(repository);
    final List<Tag> tags = List.of(Tag.builder().id(UUID.randomUUID()).nome("rock").build());

    when(repository.consultar()).thenReturn(tags);

    final var response = useCase.execute();

    assertEquals(tags, response);
    verify(repository, times(1)).consultar();
  }
}
