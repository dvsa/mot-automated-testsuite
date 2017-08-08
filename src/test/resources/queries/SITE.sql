SELECT s.name as site_name, s.site_number
FROM site s, site_contact_detail_map d_map, mot_test_current mtc
WHERE s.id = d_map.site_id
AND d_map.contact_detail_id IS NOT NULL -- Must not be null as there's a bug with replacement certificates
AND s.site_number != "0"
AND s.type_id = 3
AND s.id = mtc.site_id
GROUP BY mtc.site_id
LIMIT 30