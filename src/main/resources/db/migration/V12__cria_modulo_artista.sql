ALTER TABLE album ADD COLUMN tipo VARCHAR(20) NOT NULL DEFAULT 'ALBUM';

ALTER TABLE usuario ADD COLUMN descricao TEXT;

CREATE TABLE artista_seguidor (
    id UUID PRIMARY KEY,
    artista_id UUID NOT NULL REFERENCES usuario(id),
    usuario_id UUID NOT NULL REFERENCES usuario(id),
    criado_por UUID,
    criado_em TIMESTAMP,
    atualizado_por UUID,
    atualizado_em TIMESTAMP,
    deletado_por UUID,
    deletado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_artista_seguidor UNIQUE (artista_id, usuario_id),
    CONSTRAINT ck_artista_seguidor_nao_e_o_proprio CHECK (artista_id <> usuario_id)
);

CREATE TABLE artista_bloqueado (
    id UUID PRIMARY KEY,
    artista_id UUID NOT NULL REFERENCES usuario(id),
    usuario_id UUID NOT NULL REFERENCES usuario(id),
    criado_por UUID,
    criado_em TIMESTAMP,
    atualizado_por UUID,
    atualizado_em TIMESTAMP,
    deletado_por UUID,
    deletado_em TIMESTAMP,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT uq_artista_bloqueado UNIQUE (artista_id, usuario_id),
    CONSTRAINT ck_artista_bloqueado_nao_e_o_proprio CHECK (artista_id <> usuario_id)
);
