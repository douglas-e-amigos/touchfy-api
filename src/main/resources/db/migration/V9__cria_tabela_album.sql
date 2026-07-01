CREATE TABLE album (
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    data_lancamento DATE,
    genero_musical_id UUID REFERENCES genero_musical(id),
    artista_id UUID NOT NULL REFERENCES usuario(id),
    criado_por UUID,
    criado_em TIMESTAMP,
    atualizado_por UUID,
    atualizado_em TIMESTAMP,
    deletado_por UUID,
    deletado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE album_musica (
    id UUID PRIMARY KEY,
    album_id UUID NOT NULL REFERENCES album(id),
    musica_id UUID NOT NULL REFERENCES musica(id),
    ordem INTEGER NOT NULL,
    criado_por UUID,
    criado_em TIMESTAMP,
    atualizado_por UUID,
    atualizado_em TIMESTAMP,
    deletado_por UUID,
    deletado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_album_musica UNIQUE (album_id, musica_id)
);

CREATE TABLE album_salvo (
    id UUID PRIMARY KEY,
    album_id UUID NOT NULL REFERENCES album(id),
    usuario_id UUID NOT NULL REFERENCES usuario(id),
    criado_por UUID,
    criado_em TIMESTAMP,
    atualizado_por UUID,
    atualizado_em TIMESTAMP,
    deletado_por UUID,
    deletado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_album_salvo UNIQUE (album_id, usuario_id)
);
