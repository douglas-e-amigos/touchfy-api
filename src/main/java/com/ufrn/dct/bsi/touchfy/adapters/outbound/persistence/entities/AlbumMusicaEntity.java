package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@SQLRestriction("ativo = true")
@Table(name = "album_musica")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class AlbumMusicaEntity extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false)
    private AlbumEntity album;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "musica_id", nullable = false)
    private MusicaEntity musica;

    @Column(name = "ordem", nullable = false)
    private Integer ordem;
}
