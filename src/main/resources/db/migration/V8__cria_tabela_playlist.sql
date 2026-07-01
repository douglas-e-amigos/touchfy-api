CREATE TABLE playlist (
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    visibilidade VARCHAR(20) NOT NULL,
    dono_id UUID NOT NULL REFERENCES usuario(id),
    criado_por UUID,
    criado_em TIMESTAMP,
    atualizado_por UUID,
    atualizado_em TIMESTAMP,
    deletado_por UUID,
    deletado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE playlist_musica (
    id UUID PRIMARY KEY,
    playlist_id UUID NOT NULL REFERENCES playlist(id),
    musica_id UUID NOT NULL REFERENCES musica(id),
    ordem INTEGER NOT NULL,
    criado_por UUID,
    criado_em TIMESTAMP,
    atualizado_por UUID,
    atualizado_em TIMESTAMP,
    deletado_por UUID,
    deletado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_playlist_musica UNIQUE (playlist_id, musica_id)
);

CREATE TABLE playlist_usuarios_convidados (
    playlist_id UUID NOT NULL REFERENCES playlist(id),
    usuario_id UUID NOT NULL REFERENCES usuario(id),
    PRIMARY KEY (playlist_id, usuario_id)
);
