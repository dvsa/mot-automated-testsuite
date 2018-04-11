SELECT DISTINCT p.username
FROM person p, mot_test_current mtc
WHERE DATE(mtc.completed_date) > date_sub(CURDATE(), INTERVAL 10 DAY)
AND mtc.mot_test_type_id IN (1,9)
AND p.id = mtc.person_id
AND p.username IS NOT NULL
LIMIT 50