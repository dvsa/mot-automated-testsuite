SELECT p.username as username
FROM person p, person_system_role_map role_map
WHERE p.id = role_map.person_id
AND role_map.person_system_role_id = 6
AND p.username IS NOT NULL
order by username asc
-- LIMIT 50