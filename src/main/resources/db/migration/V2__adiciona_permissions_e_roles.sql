INSERT INTO roles (name) VALUES
    ('ADMIN'),
    ('MODERADOR'),
    ('ARTISTA'),
    ('OUVINTE');

INSERT INTO permissions (name) VALUES
    ('music:create'),
    ('music:read'),
    ('music:update'),
    ('music:delete'),
    ('album:create'),
    ('album:read'),
    ('album:update'),
    ('album:delete'),
    ('playlist:create'),
    ('playlist:read'),
    ('playlist:update'),
    ('playlist:delete'),
    ('artista:create'),
    ('artista:read'),
    ('artista:update'),
    ('artista:delete');

-- Atribui todas as permissões ao administrador
INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ADMIN' AND p.name IN (
    'music:create', 'music:read', 'music:update', 'music:delete',
    'album:create', 'album:read', 'album:update', 'album:delete',
    'playlist:create', 'playlist:read', 'playlist:update', 'playlist:delete',
    'artista:create', 'artista:read', 'artista:update', 'artista:delete'
);

-- ARTISTA: pode gerenciar seu próprio conteúdo
INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ARTISTA' AND p.name IN (
    'music:create', 'music:read', 'music:update', 'music:delete',
    'album:create', 'album:read', 'album:update', 'album:delete',
    'artista:read', 'artista:update', 'artista:delete'
);

-- OUVINTE: só leitura + gerenciar suas próprias playlists
INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'OUVINTE' AND p.name IN (
    'music:read',
    'album:read',
    'playlist:create', 'playlist:read', 'playlist:update', 'playlist:delete',
    'artista:read'
);

-- MODERADOR: modera conteúdo, mas não deleta artistas
INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'MODERADOR' AND p.name IN (
    'music:read', 'music:update', 'music:delete',
    'album:read', 'album:update', 'album:delete',
    'playlist:read',
    'artista:read', 'artista:update'
);