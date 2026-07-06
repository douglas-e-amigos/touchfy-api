INSERT INTO permissions (name) VALUES
    ('file:upload'),
    ('file:read'),
    ('file:update'),
    ('file:delete'),
    ('permission:create'),
    ('permission:read'),
    ('permission:update'),
    ('permission:delete'),
    ('role:create'),
    ('role:read'),
    ('role:update'),
    ('role:delete');

-- Atribui todas as roles ao administrador
INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ADMIN' AND p.name IN (
    'file:upload', 'file:read', 'file:update', 'file:delete',
    'permission:create', 'permission:read', 'permission:update', 'permission:delete',
    'role:create', 'role:read', 'role:update', 'role:delete'
);