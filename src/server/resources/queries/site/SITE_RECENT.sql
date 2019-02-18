SELECT DISTINCT s.site_number, s.name as site_name
FROM site s, site_contact_detail_map d_map, mot_test_current mtc
INNER JOIN (SELECT site_id, MAX(id) AS id -- Select the id of the latest MOT that is a Normal Test or Re-Test
			FROM mot_test_current
	 	    WHERE created_on > CURRENT_DATE - INTERVAL 3 WEEK -- only current certificates can be pulled
 	   		GROUP BY site_id) mtcId
 	   		ON mtc.id = mtcId.id
WHERE s.id = mtc.site_id
AND mtc.mot_test_type_id IN (1,9)
AND d_map.site_id = s.id
AND d_map.contact_detail_id IS NOT NULL
AND s.site_number != "0"
AND s.type_id = 3
LIMIT 30
