SELECT DISTINCT s.site_number, s.name as site_name
FROM site s
JOIN ( SELECT max(submitted_date), site_id
           FROM mot_test_current
           WHERE completed_date > date_sub(CURDATE(), INTERVAL 2 MONTH)
           GROUP BY site_id
           ) mtc
           ON s.id = mtc.site_id
JOIN ( SELECT site_id FROM site_contact_detail_map
		   WHERE contact_detail_id IS NOT NULL
		   ) d_map
		   ON s.id =d_map.site_id
JOIN mot_test_current lmtc ON lmtc.site_id = s.id
WHERE s.site_number > "0"
AND lmtc.mot_test_type_id IN (1, 9) -- Normal Test or Re-Test
AND s.type_id = 3
LIMIT 30