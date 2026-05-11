-- Migração V4: adiciona coluna 'ativo' em todas as tabelas dos módulos
-- Usamos ALTER TABLE para não recriar tabelas já existentes

ALTER TABLE usuario ADD COLUMN ativo BOOLEAN DEFAULT TRUE;

ALTER TABLE permissao ADD COLUMN ativo BOOLEAN DEFAULT TRUE;

ALTER TABLE papel ADD COLUMN ativo BOOLEAN DEFAULT TRUE;

ALTER TABLE permissao_do_usuario ADD COLUMN ativo BOOLEAN DEFAULT TRUE;

ALTER TABLE papel_do_usuario ADD COLUMN ativo BOOLEAN DEFAULT TRUE;

ALTER TABLE permissao_do_papel ADD COLUMN ativo BOOLEAN DEFAULT TRUE;

ALTER TABLE usuario_bloquado ADD COLUMN ativo BOOLEAN DEFAULT TRUE;
