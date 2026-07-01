package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.MusicaEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.PlaylistEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.PlaylistMusicaEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities.UsuarioEntity;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers.PlaylistMapper;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.MusicaJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.PlaylistJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.PlaylistMusicaJpaRepository;
import com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories.jpa.UsuarioJpaRepository;
import com.ufrn.dct.bsi.touchfy.application.dtos.playlist.CriarPlaylistRequest;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Playlist;
import com.ufrn.dct.bsi.touchfy.domain.playlist.models.Visibilidade;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.AuditorAware;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlaylistRepositoryImplTest {

    @Test
    void deveCriarPlaylistComConvidados() {
        final AuditorAware<UUID> auditorAware = Optional::empty;
        final PlaylistJpaRepository jpaRepository = mock(PlaylistJpaRepository.class);
        final PlaylistMusicaJpaRepository playlistMusicaJpaRepository = mock(PlaylistMusicaJpaRepository.class);
        final MusicaJpaRepository musicaJpaRepository = mock(MusicaJpaRepository.class);
        final UsuarioJpaRepository usuarioJpaRepository = mock(UsuarioJpaRepository.class);
        final PlaylistMapper mapper = mock(PlaylistMapper.class);
        final PlaylistRepositoryImpl repository = new PlaylistRepositoryImpl(
                auditorAware, jpaRepository, playlistMusicaJpaRepository,
                musicaJpaRepository, usuarioJpaRepository, mapper);

        final UUID id = UUID.randomUUID();
        final UUID donoId = UUID.randomUUID();
        final UUID convidadoId = UUID.randomUUID();
        final var convidadoEntity = UsuarioEntity.builder().id(convidadoId).build();
        final var request = new CriarPlaylistRequest("Minha Playlist", "Desc",
                Visibilidade.PUBLICA, List.of(convidadoId));

        when(usuarioJpaRepository.findAllById(List.of(convidadoId))).thenReturn(List.of(convidadoEntity));

        repository.criar(id, request, donoId);

        final var captor = ArgumentCaptor.forClass(PlaylistEntity.class);
        verify(jpaRepository).save(captor.capture());

        final var entity = captor.getValue();
        assertEquals("Minha Playlist", entity.getNome());
        assertEquals(Visibilidade.PUBLICA, entity.getVisibilidade());
        assertEquals(donoId, entity.getDonoId());
        assertTrue(entity.getUsuariosConvidados().contains(convidadoEntity));
    }

    @Test
    void deveAdicionarMusicaNaPlaylist() {
        final AuditorAware<UUID> auditorAware = Optional::empty;
        final PlaylistJpaRepository jpaRepository = mock(PlaylistJpaRepository.class);
        final PlaylistMusicaJpaRepository playlistMusicaJpaRepository = mock(PlaylistMusicaJpaRepository.class);
        final MusicaJpaRepository musicaJpaRepository = mock(MusicaJpaRepository.class);
        final UsuarioJpaRepository usuarioJpaRepository = mock(UsuarioJpaRepository.class);
        final PlaylistMapper mapper = mock(PlaylistMapper.class);
        final PlaylistRepositoryImpl repository = new PlaylistRepositoryImpl(
                auditorAware, jpaRepository, playlistMusicaJpaRepository,
                musicaJpaRepository, usuarioJpaRepository, mapper);

        final UUID playlistId = UUID.randomUUID();
        final UUID musicaId = UUID.randomUUID();
        final var playlistEntity = PlaylistEntity.builder()
                .id(playlistId).nome("Playlist").visibilidade(Visibilidade.PUBLICA)
                .donoId(UUID.randomUUID()).build();
        final var musicaEntity = MusicaEntity.builder()
                .id(musicaId).nome("Música").caminhoDoArquivo("path").build();

        when(jpaRepository.findById(playlistId)).thenReturn(Optional.of(playlistEntity));
        when(musicaJpaRepository.findById(musicaId)).thenReturn(Optional.of(musicaEntity));

        repository.adicionarMusica(playlistId, musicaId, 0);

        verify(jpaRepository).save(playlistEntity);
        assertEquals(1, playlistEntity.getMusicas().size());
        assertEquals(Integer.valueOf(0), playlistEntity.getMusicas().get(0).getOrdem());
    }

    @Test
    void deveDeletarPlaylistComSoftDelete() {
        final UUID auditorId = UUID.randomUUID();
        final AuditorAware<UUID> auditorAware = () -> Optional.of(auditorId);
        final PlaylistJpaRepository jpaRepository = mock(PlaylistJpaRepository.class);
        final PlaylistMusicaJpaRepository playlistMusicaJpaRepository = mock(PlaylistMusicaJpaRepository.class);
        final MusicaJpaRepository musicaJpaRepository = mock(MusicaJpaRepository.class);
        final UsuarioJpaRepository usuarioJpaRepository = mock(UsuarioJpaRepository.class);
        final PlaylistMapper mapper = mock(PlaylistMapper.class);
        final PlaylistRepositoryImpl repository = new PlaylistRepositoryImpl(
                auditorAware, jpaRepository, playlistMusicaJpaRepository,
                musicaJpaRepository, usuarioJpaRepository, mapper);

        final UUID id = UUID.randomUUID();
        final var entity = PlaylistEntity.builder()
                .id(id).nome("Playlist").visibilidade(Visibilidade.PUBLICA)
                .donoId(UUID.randomUUID()).build();

        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        repository.deletar(id);

        assertFalse(entity.getAtivo());
        assertEquals(auditorId, entity.getDeletadoPor());
        assertNotNull(entity.getDeletadoEm());
        verify(jpaRepository).save(entity);
    }

    @Test
    void deveRemoverMusicaComSoftDelete() {
        final UUID auditorId = UUID.randomUUID();
        final AuditorAware<UUID> auditorAware = () -> Optional.of(auditorId);
        final PlaylistJpaRepository jpaRepository = mock(PlaylistJpaRepository.class);
        final PlaylistMusicaJpaRepository playlistMusicaJpaRepository = mock(PlaylistMusicaJpaRepository.class);
        final MusicaJpaRepository musicaJpaRepository = mock(MusicaJpaRepository.class);
        final UsuarioJpaRepository usuarioJpaRepository = mock(UsuarioJpaRepository.class);
        final PlaylistMapper mapper = mock(PlaylistMapper.class);
        final PlaylistRepositoryImpl repository = new PlaylistRepositoryImpl(
                auditorAware, jpaRepository, playlistMusicaJpaRepository,
                musicaJpaRepository, usuarioJpaRepository, mapper);

        final UUID playlistId = UUID.randomUUID();
        final UUID musicaId = UUID.randomUUID();
        final var relacao = PlaylistMusicaEntity.builder()
                .playlist(PlaylistEntity.builder().build())
                .musica(MusicaEntity.builder().build())
                .ordem(0).build();

        when(playlistMusicaJpaRepository.findByPlaylistIdAndMusicaId(playlistId, musicaId))
                .thenReturn(Optional.of(relacao));

        repository.removerMusica(playlistId, musicaId);

        assertFalse(relacao.getAtivo());
        assertEquals(auditorId, relacao.getDeletadoPor());
        assertNotNull(relacao.getDeletadoEm());
        verify(playlistMusicaJpaRepository).save(relacao);
    }

    @Test
    void deveRetornarPlaylistsVisiveisParaUsuario() {
        final AuditorAware<UUID> auditorAware = Optional::empty;
        final PlaylistJpaRepository jpaRepository = mock(PlaylistJpaRepository.class);
        final PlaylistMusicaJpaRepository playlistMusicaJpaRepository = mock(PlaylistMusicaJpaRepository.class);
        final MusicaJpaRepository musicaJpaRepository = mock(MusicaJpaRepository.class);
        final UsuarioJpaRepository usuarioJpaRepository = mock(UsuarioJpaRepository.class);
        final PlaylistMapper mapper = mock(PlaylistMapper.class);
        final PlaylistRepositoryImpl repository = new PlaylistRepositoryImpl(
                auditorAware, jpaRepository, playlistMusicaJpaRepository,
                musicaJpaRepository, usuarioJpaRepository, mapper);

        final UUID usuarioId = UUID.randomUUID();
        final var publica = PlaylistEntity.builder()
                .id(UUID.randomUUID()).nome("Pública").visibilidade(Visibilidade.PUBLICA)
                .donoId(UUID.randomUUID()).build();
        final var privadaDoUsuario = PlaylistEntity.builder()
                .id(UUID.randomUUID()).nome("Minha").visibilidade(Visibilidade.PRIVADA)
                .donoId(usuarioId).build();
        final var privadaDeOutro = PlaylistEntity.builder()
                .id(UUID.randomUUID()).nome("Outro").visibilidade(Visibilidade.PRIVADA)
                .donoId(UUID.randomUUID()).build();
        final var protegida = PlaylistEntity.builder()
                .id(UUID.randomUUID()).nome("Protegida").visibilidade(Visibilidade.PROTEGIDA)
                .donoId(UUID.randomUUID()).usuariosConvidados(Set.of(
                        UsuarioEntity.builder().id(usuarioId).build()))
                .build();

        when(jpaRepository.findAll()).thenReturn(List.of(publica, privadaDoUsuario, privadaDeOutro, protegida));
        when(mapper.toDomain(any())).thenReturn(mock(Playlist.class));

        final var resultado = repository.listarVisiveisParaUsuario(usuarioId);

        assertEquals(3, resultado.size());
    }
}
