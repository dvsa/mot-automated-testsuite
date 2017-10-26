SELECT s.site_number, s.id, s.name, a.town, a.postcode
FROM site s, site_contact_detail_map scdm, contact_detail cd, address a
WHERE scdm.site_id = s.id
AND cd.id = scdm.contact_detail_id
AND a.id = cd.address_id
ORDER BY s.name ASC
LIMIT 30;