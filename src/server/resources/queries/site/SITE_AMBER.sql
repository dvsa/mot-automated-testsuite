SELECT DISTINCT s.site_number, srs.risk_colour, s.name, o.name
FROM site s, site_risk_score srs, organisation o
WHERE s.organisation_id IS NOT NULL
AND s.organisation_id = o.id
AND s.id = srs.site_id
AND srs.risk_colour = "AMBER"
LIMIT 30