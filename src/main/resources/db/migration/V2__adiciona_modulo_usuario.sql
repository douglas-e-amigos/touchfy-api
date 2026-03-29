CREATE TABLE IF NOT EXISTS usuario(
    id UUID PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    nome_usuario VARCHAR(200) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    data_nascimento DATE NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    deletado_em TIMESTAMP DEFAULT NULL,
    criado_por UUID,
    atualizado_por UUID,
    deletado_por UUID NULL
);

CREATE TABLE IF NOT EXISTS permissao(
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    identificador VARCHAR(100) NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    deletado_em TIMESTAMP DEFAULT NULL,
    criado_por UUID,
    atualizado_por UUID,
    deletado_por UUID NULL
);

CREATE TABLE IF NOT EXISTS papel(
    id UUID PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    identificador VARCHAR(100) NOT NULL,
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    deletado_em TIMESTAMP DEFAULT NULL,
    criado_por UUID,
    atualizado_por UUID,
    deletado_por UUID NULL
);

CREATE TABLE IF NOT EXISTS permissao_do_usuario(
    id UUID PRIMARY KEY,
    permissao_id UUID NOT NULL,
    usuario_id UUID NOT NULL,
    FOREIGN KEY (permissao_id) REFERENCES permissao(id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    deletado_em TIMESTAMP DEFAULT NULL,
    criado_por UUID,
    atualizado_por UUID,
    deletado_por UUID NULL
);

CREATE TABLE IF NOT EXISTS papel_do_usuario(
    id UUID PRIMARY KEY,
    papel_id UUID NOT NULL,
    usuario_id UUID NOT NULL,
    FOREIGN KEY (papel_id) REFERENCES papel(id),
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    deletado_em TIMESTAMP DEFAULT NULL,
    criado_por UUID,
    atualizado_por UUID,
    deletado_por UUID NULL
);

CREATE TABLE IF NOT EXISTS permissao_do_papel(
    id UUID PRIMARY KEY,
    papel_id UUID NOT NULL,
    permissao_id UUID NOT NULL,
    FOREIGN KEY (permissao_id) REFERENCES permissao(id),
    FOREIGN KEY (papel_id) REFERENCES papel(id),
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    deletado_em TIMESTAMP DEFAULT NULL,
    criado_por UUID,
    atualizado_por UUID,
    deletado_por UUID NULL
);

CREATE TABLE IF NOT EXISTS usuario_bloquado(
    id UUID PRIMARY KEY,
    usuario_id UUID NOT NULL,
    usuario_bloqueado_id UUID NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (usuario_bloqueado_id) REFERENCES usuario(id),
    CHECK (usuario_id <> usuario_bloqueado_id),
    criado_em TIMESTAMP,
    atualizado_em TIMESTAMP,
    deletado_em TIMESTAMP DEFAULT NULL,
    criado_por UUID,
    atualizado_por UUID,
    deletado_por UUID NULL
);