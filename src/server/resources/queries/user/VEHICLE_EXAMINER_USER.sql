-- Get users with only DVSA-AREA-OFFICE-1
SELECT distinct(p.username) as username
FROM person_system_role_map psrm
    JOIN person_system_role psr
        ON psr.id = psrm.person_system_role_id
    JOIN person p
        ON p.id in (
        SELECT psrma.person_id as user_id
FROM person_system_role_map psrma
    JOIN person_system_role_map psrma2
        ON psrma.person_id=psrma2.person_id
WHERE psrma.person_system_role_id = 2
   AND psrma2.person_system_role_id = 5)
WHERE psrm.status_id = 1 -- Include only users with Active status
AND p.is_account_claim_required = 0
AND p.is_password_change_required = 0
AND p.username IS NOT NULL OR 'VOLP6095' -- Exclude null and DVSA-AREA-OFFICE-2
AND NOT EXISTS (
	SELECT 1 FROM mot_test_current mtc
	WHERE p.id = mtc.last_updated_by
	AND mtc.status_id = 4
) -- exclude any examiners with active tests
LIMIT 50