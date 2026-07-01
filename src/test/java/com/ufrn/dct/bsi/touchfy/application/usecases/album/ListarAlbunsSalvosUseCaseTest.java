package com.ufrn.dct.bsi.touchfy.application.usecases.album;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.AlbumMapper;
import com.ufrn.dct.bsi.touchfy.application.dtos.album.AlbumSalvoResponse;
import com.ufrn.dct.bsi.touchfy.domain.album.models.Album;
import com.ufrn.dct.bsi.touchfy.domain.album.models.AlbumSalvo;
import com.ufrn.dct.bsi.touchfy.domain.album.repositories.AlbumRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ListarAlbunsSalvosUseCaseTest {

    @Test
    void deveListarAlbunsSalvosDoUsuario() {
        final AlbumRepository repository = mock(AlbumRepository.class);
        final AlbumMapper mapper = mock(AlbumMapper.class);
        final ListarAlbunsSalvosUseCase useCase = new ListarAlbunsSalvosUseCase(repository, mapper);
        final UUID usuarioId = UUID.randomUUID();
        final var album = new Album(UUID.randomUUID(), "Album", null, null, null,
                UUID.randomUUID(), null);
        final var salvo = new AlbumSalvo(UUID.randomUUID(), album, usuarioId);

        when(repository.listarSalvos(usuarioId)).thenReturn(List.of(salvo));
        when(mapper.toSalvoResponse(salvo)).thenReturn(mock(AlbumSalvoResponse.class));

        final var resultado = useCase.execute(usuarioId);

        assertEquals(1, resultado.size());
    }
}
