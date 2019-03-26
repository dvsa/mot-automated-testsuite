SELECT DISTINCT s.site_number, srs.risk_colour, s.name, o.name
FROM site s, site_risk_score srs, organisation o, organisation_site_map osm
WHERE s.organisation_id IS NOT NULL
AND s.organisation_id = o.id
AND s.organisation_id = osm.organisation_id
AND s.id = osm.site_id
AND osm.status_id = 2
AND s.id = srs.site_id
AND srs.risk_colour = "RED"
AND srs.extract_date > DATE(NOW()-INTERVAL 5 YEAR)
LIMIT 30