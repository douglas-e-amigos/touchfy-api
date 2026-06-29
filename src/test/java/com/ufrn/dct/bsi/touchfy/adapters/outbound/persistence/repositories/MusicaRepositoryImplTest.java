package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.GeneroDaMusicaEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.GeneroMusicalEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.MusicaDaTagEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.MusicaEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.TagEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.MusicaMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.GeneroMusicalJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.MusicaJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.TagJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.UsuarioJpaRepository;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.AtualizarMusicaRequest;
import com.ufrn.dct.bsi.touchfy.application.dtos.musicas.CriarMusicaRequest;
import com.ufrn.dct.bsi.touchfy.domain.musica.models.Musica;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

class MusicaRepositoryImplTest {

    @Test
    void deveSalvarMusicaComRelacionamentos() {
        final AuditorAware<UUID> auditorAware = Optional::<UUID>empty;
        final MusicaJpaRepository jpaRepository = mock(MusicaJpaRepository.class);
        final TagJpaRepository tagJpaRepository = mock(TagJpaRepository.class);
        final GeneroMusicalJpaRepository generoMusicalJpaRepository = mock(GeneroMusicalJpaRepository.class);
        final UsuarioJpaRepository usuarioJpaRepository = mock(UsuarioJpaRepository.class);
        final MusicaMapper mapper = mock(MusicaMapper.class);
        final MusicaRepositoryImpl repository = new MusicaRepositoryImpl(
                auditorAware,
                jpaRepository,
                tagJpaRepository,
                generoMusicalJpaRepository,
                usuarioJpaRepository,
                mapper
        );
        final UUID tagId = UUID.randomUUID();
        final UUID generoId = UUID.randomUUID();
        final CriarMusicaRequest request = new CriarMusicaRequest(
                "Tempo Perdido",
                "Letra",
                List.of(tagId),
                List.of(generoId),
                new MockMultipartFile("arquivo", "tempo-perdido.mp3", "audio/mpeg", "abc".getBytes())
        );
        final TagEntity tagEntity = TagEntity.builder().id(tagId).nome("Rock").build();
        final GeneroMusicalEntity generoEntity = GeneroMusicalEntity.builder()
                .id(generoId)
                .nome("Rock Nacional")
                .build();

        when(tagJpaRepository.findAllById(List.of(tagId))).thenReturn(List.of(tagEntity));
        when(generoMusicalJpaRepository.findAllById(List.of(generoId))).thenReturn(List.of(generoEntity));

        repository.salvar(request, "musicas/arquivo.mp3");

        final ArgumentCaptor<MusicaEntity> captor = ArgumentCaptor.forClass(MusicaEntity.class);
        verify(jpaRepository).save(captor.capture());

        final MusicaEntity entity = captor.getValue();
        assertEquals("Tempo Perdido", entity.getNome());
        assertEquals("musicas/arquivo.mp3", entity.getCaminhoDoArquivo());
        assertEquals(1, entity.getTags().size());
        assertEquals(tagId, entity.getTags().get(0).getTag().getId());
        assertEquals(1, entity.getGenerosMusicais().size());
        assertEquals(generoId, entity.getGenerosMusicais().get(0).getGeneroMusical().getId());
    }

    @Test
    void deveConsultarMusicasMapeandoEntidadesParaDominio() {
        final AuditorAware<UUID> auditorAware = Optional::<UUID>empty;
        final MusicaJpaRepository jpaRepository = mock(MusicaJpaRepository.class);
        final TagJpaRepository tagJpaRepository = mock(TagJpaRepository.class);
        final GeneroMusicalJpaRepository generoMusicalJpaRepository = mock(GeneroMusicalJpaRepository.class);
        final UsuarioJpaRepository usuarioJpaRepository = mock(UsuarioJpaRepository.class);
        final MusicaMapper mapper = mock(MusicaMapper.class);
        final MusicaRepositoryImpl repository = new MusicaRepositoryImpl(
                auditorAware,
                jpaRepository,
                tagJpaRepository,
                generoMusicalJpaRepository,
                usuarioJpaRepository,
                mapper
        );
        final UUID artistaId = UUID.randomUUID();
        final MusicaEntity entity = MusicaEntity.builder()
                .id(UUID.randomUUID())
                .nome("Tempo Perdido")
                .caminhoDoArquivo("musicas/arquivo.mp3")
                .criadoPor(artistaId)
                .build();
        final Musica musica = Musica.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .caminhoDoArquivo(entity.getCaminhoDoArquivo())
                .criadoPor(artistaId)
                .build();
        final UsuarioEntity artista = UsuarioEntity.builder()
                .id(artistaId)
                .nome("João")
                .nomeUsuario("joao")
                .build();

        when(jpaRepository.findAll()).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(musica);
        when(usuarioJpaRepository.findAllById(List.of(artistaId))).thenReturn(List.of(artista));

        final List<Musica> resultado = repository.consultar();

        assertEquals(List.of(musica), resultado);
        assertEquals("João", resultado.getFirst().getArtistaNome());
        assertEquals("joao", resultado.getFirst().getArtistaNomeUsuario());
        verify(mapper, times(1)).toDomain(entity);
    }

    @Test
    void deveConsultarMusicasPorCriadoPor() {
        final AuditorAware<UUID> auditorAware = Optional::<UUID>empty;
        final MusicaJpaRepository jpaRepository = mock(MusicaJpaRepository.class);
        final TagJpaRepository tagJpaRepository = mock(TagJpaRepository.class);
        final GeneroMusicalJpaRepository generoMusicalJpaRepository = mock(GeneroMusicalJpaRepository.class);
        final UsuarioJpaRepository usuarioJpaRepository = mock(UsuarioJpaRepository.class);
        final MusicaMapper mapper = mock(MusicaMapper.class);
        final MusicaRepositoryImpl repository = new MusicaRepositoryImpl(
                auditorAware,
                jpaRepository,
                tagJpaRepository,
                generoMusicalJpaRepository,
                usuarioJpaRepository,
                mapper
        );
        final UUID artistaId = UUID.randomUUID();
        final MusicaEntity entity = MusicaEntity.builder()
                .id(UUID.randomUUID())
                .nome("Tempo Perdido")
                .caminhoDoArquivo("musicas/arquivo.mp3")
                .criadoPor(artistaId)
                .build();
        final Musica musica = Musica.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .caminhoDoArquivo(entity.getCaminhoDoArquivo())
                .criadoPor(artistaId)
                .build();
        final UsuarioEntity artista = UsuarioEntity.builder()
                .id(artistaId)
                .nome("João")
                .nomeUsuario("joao")
                .build();

        when(jpaRepository.findByCriadoPor(artistaId)).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(musica);
        when(usuarioJpaRepository.findAllById(List.of(artistaId))).thenReturn(List.of(artista));

        final List<Musica> resultado = repository.consultarPorCriadoPor(artistaId);

        assertEquals(List.of(musica), resultado);
        assertEquals("João", resultado.getFirst().getArtistaNome());
        verify(jpaRepository, times(1)).findByCriadoPor(artistaId);
    }

    @Test
    void deveConsultarMusicasPorNomeDoArtista() {
        final AuditorAware<UUID> auditorAware = Optional::<UUID>empty;
        final MusicaJpaRepository jpaRepository = mock(MusicaJpaRepository.class);
        final TagJpaRepository tagJpaRepository = mock(TagJpaRepository.class);
        final GeneroMusicalJpaRepository generoMusicalJpaRepository = mock(GeneroMusicalJpaRepository.class);
        final UsuarioJpaRepository usuarioJpaRepository = mock(UsuarioJpaRepository.class);
        final MusicaMapper mapper = mock(MusicaMapper.class);
        final MusicaRepositoryImpl repository = new MusicaRepositoryImpl(
                auditorAware,
                jpaRepository,
                tagJpaRepository,
                generoMusicalJpaRepository,
                usuarioJpaRepository,
                mapper
        );
        final UUID artistaId = UUID.randomUUID();
        final UsuarioEntity artista = UsuarioEntity.builder()
                .id(artistaId)
                .nome("João")
                .nomeUsuario("joao")
                .build();
        final MusicaEntity entity = MusicaEntity.builder()
                .id(UUID.randomUUID())
                .nome("Tempo Perdido")
                .caminhoDoArquivo("musicas/arquivo.mp3")
                .criadoPor(artistaId)
                .build();
        final Musica musica = Musica.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .caminhoDoArquivo(entity.getCaminhoDoArquivo())
                .criadoPor(artistaId)
                .build();

        when(usuarioJpaRepository.findByNomeContainingIgnoreCaseOrNomeUsuarioContainingIgnoreCase("jo", "jo"))
                .thenReturn(List.of(artista));
        when(jpaRepository.findByCriadoPorIn(List.of(artistaId))).thenReturn(List.of(entity));
        when(mapper.toDomain(entity)).thenReturn(musica);
        when(usuarioJpaRepository.findAllById(List.of(artistaId))).thenReturn(List.of(artista));

        final List<Musica> resultado = repository.consultarPorNomeArtista("jo");

        assertEquals(List.of(musica), resultado);
        assertEquals("João", resultado.getFirst().getArtistaNome());
        verify(jpaRepository, times(1)).findByCriadoPorIn(List.of(artistaId));
    }

    @Test
    void deveAcharMusicaPeloId() {
        final AuditorAware<UUID> auditorAware = Optional::<UUID>empty;
        final MusicaJpaRepository jpaRepository = mock(MusicaJpaRepository.class);
        final TagJpaRepository tagJpaRepository = mock(TagJpaRepository.class);
        final GeneroMusicalJpaRepository generoMusicalJpaRepository = mock(GeneroMusicalJpaRepository.class);
        final UsuarioJpaRepository usuarioJpaRepository = mock(UsuarioJpaRepository.class);
        final MusicaMapper mapper = mock(MusicaMapper.class);
        final MusicaRepositoryImpl repository = new MusicaRepositoryImpl(
                auditorAware,
                jpaRepository,
                tagJpaRepository,
                generoMusicalJpaRepository,
                usuarioJpaRepository,
                mapper
        );
        final UUID id = UUID.randomUUID();
        final MusicaEntity entity = MusicaEntity.builder()
                .id(id)
                .nome("Tempo Perdido")
                .caminhoDoArquivo("musicas/arquivo.mp3")
                .build();
        final Musica musica = Musica.builder()
                .id(id)
                .nome("Tempo Perdido")
                .caminhoDoArquivo("musicas/arquivo.mp3")
                .build();

        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.toDomain(entity)).thenReturn(musica);

        final Optional<Musica> resultado = repository.acharPeloId(id);

        assertTrue(resultado.isPresent());
        assertEquals(musica, resultado.get());
    }

    @Test
    void deveAtualizarMusicaERelacionamentosQuandoEncontrada() {
        final AuditorAware<UUID> auditorAware = Optional::<UUID>empty;
        final MusicaJpaRepository jpaRepository = mock(MusicaJpaRepository.class);
        final TagJpaRepository tagJpaRepository = mock(TagJpaRepository.class);
        final GeneroMusicalJpaRepository generoMusicalJpaRepository = mock(GeneroMusicalJpaRepository.class);
        final UsuarioJpaRepository usuarioJpaRepository = mock(UsuarioJpaRepository.class);
        final MusicaMapper mapper = mock(MusicaMapper.class);
        final MusicaRepositoryImpl repository = new MusicaRepositoryImpl(
                auditorAware,
                jpaRepository,
                tagJpaRepository,
                generoMusicalJpaRepository,
                usuarioJpaRepository,
                mapper
        );
        final UUID id = UUID.randomUUID();
        final UUID tagId = UUID.randomUUID();
        final UUID generoId = UUID.randomUUID();
        final MusicaEntity entity = MusicaEntity.builder()
                .id(id)
                .nome("Antiga")
                .caminhoDoArquivo("musicas/antiga.mp3")
                .tags(new java.util.ArrayList<>(List.of(MusicaDaTagEntity.builder()
                        .musica(MusicaEntity.builder().id(id).build())
                        .tag(TagEntity.builder().id(UUID.randomUUID()).nome("Pop").build())
                        .build())))
                .generosMusicais(new java.util.ArrayList<>(List.of(GeneroDaMusicaEntity.builder()
                        .musica(MusicaEntity.builder().id(id).build())
                        .generoMusical(GeneroMusicalEntity.builder().id(UUID.randomUUID()).nome("Balada").build())
                        .build())))
                .build();
        final AtualizarMusicaRequest request = new AtualizarMusicaRequest(
                "Nova",
                "Nova letra",
                List.of(tagId),
                List.of(generoId),
                null
        );
        final TagEntity tagEntity = TagEntity.builder().id(tagId).nome("Rock").build();
        final GeneroMusicalEntity generoEntity = GeneroMusicalEntity.builder()
                .id(generoId)
                .nome("Rock Nacional")
                .build();

        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));
        when(tagJpaRepository.findAllById(List.of(tagId))).thenReturn(List.of(tagEntity));
        when(generoMusicalJpaRepository.findAllById(List.of(generoId))).thenReturn(List.of(generoEntity));

        repository.atualizar(id, request, "musicas/nova.mp3");

        assertEquals("Nova", entity.getNome());
        assertEquals("Nova letra", entity.getLetra());
        assertEquals("musicas/nova.mp3", entity.getCaminhoDoArquivo());
        assertEquals(1, entity.getTags().size());
        assertEquals(tagId, entity.getTags().get(0).getTag().getId());
        assertEquals(1, entity.getGenerosMusicais().size());
        assertEquals(generoId, entity.getGenerosMusicais().get(0).getGeneroMusical().getId());
        verify(jpaRepository).save(entity);
    }

    @Test
    void deveLancarExcecaoQuandoMusicaNaoForEncontradaParaAtualizacao() {
        final AuditorAware<UUID> auditorAware = Optional::<UUID>empty;
        final MusicaJpaRepository jpaRepository = mock(MusicaJpaRepository.class);
        final TagJpaRepository tagJpaRepository = mock(TagJpaRepository.class);
        final GeneroMusicalJpaRepository generoMusicalJpaRepository = mock(GeneroMusicalJpaRepository.class);
        final UsuarioJpaRepository usuarioJpaRepository = mock(UsuarioJpaRepository.class);
        final MusicaMapper mapper = mock(MusicaMapper.class);
        final MusicaRepositoryImpl repository = new MusicaRepositoryImpl(
                auditorAware,
                jpaRepository,
                tagJpaRepository,
                generoMusicalJpaRepository,
                usuarioJpaRepository,
                mapper
        );
        final UUID id = UUID.randomUUID();

        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        final RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> repository.atualizar(id, new AtualizarMusicaRequest(null, null, null, null, null), null)
        );

        assertEquals("Música não encontrada.", exception.getMessage());
    }

    @Test
    void deveDeletarMusicaComSoftDeleteQuandoEncontrada() {
        final UUID auditorId = UUID.randomUUID();
        final AuditorAware<UUID> auditorAware = () -> Optional.of(auditorId);
        final MusicaJpaRepository jpaRepository = mock(MusicaJpaRepository.class);
        final TagJpaRepository tagJpaRepository = mock(TagJpaRepository.class);
        final GeneroMusicalJpaRepository generoMusicalJpaRepository = mock(GeneroMusicalJpaRepository.class);
        final UsuarioJpaRepository usuarioJpaRepository = mock(UsuarioJpaRepository.class);
        final MusicaMapper mapper = mock(MusicaMapper.class);
        final MusicaRepositoryImpl repository = new MusicaRepositoryImpl(
                auditorAware,
                jpaRepository,
                tagJpaRepository,
                generoMusicalJpaRepository,
                usuarioJpaRepository,
                mapper
        );
        final UUID id = UUID.randomUUID();
        final MusicaEntity entity = MusicaEntity.builder()
                .id(id)
                .nome("Tempo Perdido")
                .caminhoDoArquivo("musicas/arquivo.mp3")
                .ativo(true)
                .build();

        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        repository.deletar(id);

        assertFalse(entity.getAtivo());
        assertEquals(auditorId, entity.getDeletadoPor());
        assertNotNull(entity.getDeletadoEm());
        verify(jpaRepository).save(entity);
    }

    @Test
    void naoDeveSalvarQuandoMusicaNaoForEncontradaAoDeletar() {
        final AuditorAware<UUID> auditorAware = Optional::<UUID>empty;
        final MusicaJpaRepository jpaRepository = mock(MusicaJpaRepository.class);
        final TagJpaRepository tagJpaRepository = mock(TagJpaRepository.class);
        final GeneroMusicalJpaRepository generoMusicalJpaRepository = mock(GeneroMusicalJpaRepository.class);
        final UsuarioJpaRepository usuarioJpaRepository = mock(UsuarioJpaRepository.class);
        final MusicaMapper mapper = mock(MusicaMapper.class);
        final MusicaRepositoryImpl repository = new MusicaRepositoryImpl(
                auditorAware,
                jpaRepository,
                tagJpaRepository,
                generoMusicalJpaRepository,
                usuarioJpaRepository,
                mapper
        );
        final UUID id = UUID.randomUUID();

        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        repository.deletar(id);

        verify(jpaRepository, never()).save(any());
    }
}
