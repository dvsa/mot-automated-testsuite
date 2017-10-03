SELECT p.username as username
FROM person p, person_system_role_map role_map
WHERE p.id = role_map.person_id
AND p.is_account_claim_required = 0
AND p.is_password_change_required = 0
AND role_map.person_system_role_id = 15 -- DVLA manager role id
AND p.username IS NOT NULL
-- LIMIT 50