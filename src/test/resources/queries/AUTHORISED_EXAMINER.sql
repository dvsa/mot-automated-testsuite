SELECT ae_ref, o.name, count(mtc.id) as slotUsage
FROM organisation o, site s, mot_test_current mtc, auth_for_ae afe
WHERE mtc.status_id = 6
AND mtc.mot_test_type_id IN (1,2,3,9,13)
AND mtc.completed_date >= SUBDATE(CURDATE(), INTERVAL 30 DAY)
AND s.id = mtc.site_id
AND o.id = s.organisation_id
AND o.registered_company_number IS NOT NULL
AND o.name IS NOT NULL
AND afe.organisation_id = o.id
GROUP BY o.id
-- LIMIT 20