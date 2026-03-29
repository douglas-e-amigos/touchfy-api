CREATE TABLE IF NOT EXISTS musica(
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    caminho_do_arquivo VARCHAR(255) NOT NULL,
    letra TEXT,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    deletado_em TIMESTAMP DEFAULT NULL,
    criado_por UUID,
    atualizado_por UUID,
    deletado_por UUID DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS tag(
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    deletado_em TIMESTAMP DEFAULT NULL,
    criado_por UUID,
    atualizado_por UUID,
    deletado_por UUID DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS genero_musical(
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    deletado_em TIMESTAMP DEFAULT NULL,
    criado_por UUID,
    atualizado_por UUID,
    deletado_por UUID DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS genero_da_musica(
    id UUID PRIMARY KEY,
    genero_musical_id UUID NOT NULL,
    musica_id UUID NOT NULL,
    FOREIGN KEY (genero_musical_id) REFERENCES genero_musical(id),
    FOREIGN KEY (musica_id) REFERENCES musica(id),
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    deletado_em TIMESTAMP DEFAULT NULL,
    criado_por UUID,
    atualizado_por UUID,
    deletado_por UUID DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS musica_da_tag(
    id UUID PRIMARY KEY,
    tag_id UUID NOT NULL,
    musica_id UUID NOT NULL,
    FOREIGN KEY (tag_id) REFERENCES tag(id),
    FOREIGN KEY (musica_id) REFERENCES musica(id),
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    deletado_em TIMESTAMP DEFAULT NULL,
    criado_por UUID,
    atualizado_por UUID,
    deletado_por UUID DEFAULT NULL
);