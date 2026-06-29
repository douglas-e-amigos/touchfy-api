package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
import com.ufrn.dct.bsi.touchfy.domain.musica.repositories.MusicaRepository;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class MusicaRepositoryImpl implements MusicaRepository {
    private final AuditorAware<UUID> auditorAware;
    private final MusicaJpaRepository jpaRepository;
    private final TagJpaRepository tagJpaRepository;
    private final GeneroMusicalJpaRepository generoMusicalJpaRepository;
    private final UsuarioJpaRepository usuarioJpaRepository;
    private final MusicaMapper mapper;

    @Override
    @Transactional
    public void salvar(final CriarMusicaRequest request, final String caminhoDoArquivo) {
        final var entity = MusicaEntity.builder()
                .nome(request.nome())
                .letra(request.letra())
                .caminhoDoArquivo(caminhoDoArquivo)
                .build();

        atualizarRelacionamentos(entity, request.tagIds(), request.generoMusicalIds());
        jpaRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Musica> consultar() {
        return mapearComArtistas(jpaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Musica> consultarPorCriadoPor(final UUID criadoPor) {
        return mapearComArtistas(jpaRepository.findByCriadoPor(criadoPor));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Musica> consultarPorNomeArtista(final String artista) {
        final List<UUID> usuarioIds = usuarioJpaRepository
                .findByNomeContainingIgnoreCaseOrNomeUsuarioContainingIgnoreCase(artista, artista)
                .stream()
                .map(UsuarioEntity::getId)
                .toList();

        if (usuarioIds.isEmpty()) {
            return List.of();
        }

        return mapearComArtistas(jpaRepository.findByCriadoPorIn(usuarioIds));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Musica> acharPeloId(final UUID id) {
        return jpaRepository.findById(id)
                .map(entity -> mapearComArtistas(List.of(entity)).getFirst());
    }

    @Transactional(readOnly = true)
    public MusicaEntity acharEntidadePeloId(final UUID id) {
        return jpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Música não encontrada."));
    }

    @Override
    @Transactional
    public void atualizar(final UUID id, final AtualizarMusicaRequest request, final String caminhoDoArquivo) {
        final var entity = acharEntidadePeloId(id);

        if (request.nome() != null) {
            entity.setNome(request.nome());
        }

        if (request.letra() != null) {
            entity.setLetra(request.letra());
        }

        if (caminhoDoArquivo != null) {
            entity.setCaminhoDoArquivo(caminhoDoArquivo);
        }

        if (request.tagIds() != null || request.generoMusicalIds() != null) {
            final var tagsIds = request.tagIds() == null
                    ? entity.getTags()
                            .stream()
                            .map(item -> item.getTag().getId())
                            .toList()
                    : request.tagIds();
            final var generoMusicaisIds = request.generoMusicalIds() == null
                    ? entity.getGenerosMusicais()
                            .stream()
                            .map(item -> item.getGeneroMusical().getId())
                            .toList()
                    : request.generoMusicalIds();
            atualizarRelacionamentos(entity, tagsIds, generoMusicaisIds);
        }

        jpaRepository.save(entity);
    }

    @Override
    @Transactional
    public void deletar(final UUID id) {
        jpaRepository.findById(id).ifPresent(entity -> {
            entity.setAtivo(false);
            entity.setDeletadoEm(LocalDateTime.now());
            auditorAware.getCurrentAuditor().ifPresent(entity::setDeletadoPor);
            jpaRepository.save(entity);
        });
    }

    private void atualizarRelacionamentos(
            final MusicaEntity entity,
            final List<UUID> tagIds,
            final List<UUID> generoMusicalIds) {
        final List<TagEntity> tags = buscarTags(tagIds);
        final List<GeneroMusicalEntity> generosMusicais = buscarGeneros(generoMusicalIds);

        entity.getTags().clear();
        tags.forEach(tag -> entity.getTags().add(MusicaDaTagEntity.builder()
                .musica(entity)
                .tag(tag)
                .build()));

        entity.getGenerosMusicais().clear();
        generosMusicais.forEach(generoMusical -> entity.getGenerosMusicais().add(GeneroDaMusicaEntity.builder()
                .musica(entity)
                .generoMusical(generoMusical)
                .build()));
    }

    private List<TagEntity> buscarTags(final List<UUID> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return List.of();
        }

        final List<TagEntity> tags = tagJpaRepository.findAllById(tagIds);
        if (tags.size() != tagIds.size()) {
            throw new RuntimeException("Uma ou mais tags não foram encontradas.");
        }

        return tags;
    }

    private List<GeneroMusicalEntity> buscarGeneros(final List<UUID> generoMusicalIds) {
        if (generoMusicalIds == null || generoMusicalIds.isEmpty()) {
            return List.of();
        }

        final List<GeneroMusicalEntity> generosMusicais = generoMusicalJpaRepository.findAllById(generoMusicalIds);
        if (generosMusicais.size() != generoMusicalIds.size()) {
            throw new RuntimeException("Um ou mais gêneros musicais não foram encontrados.");
        }

        return generosMusicais;
    }

    private List<Musica> mapearComArtistas(final List<MusicaEntity> entities) {
        final List<Musica> musicas = entities.stream()
                .map(mapper::toDomain)
                .toList();
        final List<UUID> artistaIds = musicas.stream()
                .map(Musica::getCriadoPor)
                .filter(java.util.Objects::nonNull)
                .distinct()
                .toList();

        if (artistaIds.isEmpty()) {
            return musicas;
        }

        final Map<UUID, UsuarioEntity> artistasPorId = usuarioJpaRepository.findAllById(artistaIds)
                .stream()
                .collect(Collectors.toMap(UsuarioEntity::getId, Function.identity()));

        musicas.forEach(musica -> preencherArtista(musica, artistasPorId.get(musica.getCriadoPor())));
        return musicas;
    }

    private void preencherArtista(final Musica musica, final UsuarioEntity artista) {
        if (artista == null) {
            return;
        }

        musica.setArtistaNome(artista.getNome());
        musica.setArtistaNomeUsuario(artista.getNomeUsuario());
    }
}
