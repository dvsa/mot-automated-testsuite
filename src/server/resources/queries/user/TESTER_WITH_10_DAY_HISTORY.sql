SELECT DISTINCT p.username
FROM person p, mot_test_current mtc
WHERE mtc.completed_date > date_sub(CURDATE(), INTERVAL 10 day)
AND mtc.mot_test_type_id IN (1,9)
AND p.id = mtc.person_id
AND mtc.document_id IS NOT NULL  -- exclude where there are no MOT certificates
AND status_id in( '5','6')
AND p.username IS NOT NULL
LIMIT 50