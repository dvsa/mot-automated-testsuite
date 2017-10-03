SELECT distinct p.username as username
FROM person p, person_system_role_map role_map, mot_test_current mtc
WHERE p.id = role_map.person_id
AND p.is_account_claim_required = 0
AND p.is_password_change_required = 0
AND role_map.person_system_role_id = 2 -- Vehicle Examiner role id
AND p.username IS NOT NULL
AND NOT EXISTS (
  SELECT 1 FROM mot_test_current mtc
  WHERE p.id = mtc.last_updated_by
  AND mtc.status_id = 4 -- exclude any examiners with active tests
)
LIMIT 50