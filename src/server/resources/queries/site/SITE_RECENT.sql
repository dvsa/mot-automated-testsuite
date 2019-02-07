SELECT DISTINCT s.site_number, s.name as site_name
FROM site s, site_contact_detail_map d_map, mot_test_current mtc
WHERE mtc.mot_test_type_id IN (1,9)
AND mtc.completed_date > date_sub(CURDATE(), INTERVAL 3 WEEK)
AND mtc.mot_test_type_id in ('1','9')
AND s.id = mtc.site_id
AND d_map.site_id = s.id
AND d_map.contact_detail_id IS NOT NULL
AND s.site_number != "0"
AND s.type_id = 3
LIMIT 30