INSERT INTO roles_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.name = 'ARTISTA'
  AND p.name IN ('music:update', 'music:delete')
  AND NOT EXISTS (
      SELECT 1
      FROM roles_permissions rp
      WHERE rp.role_id = r.id
        AND rp.permission_id = p.id
  );
