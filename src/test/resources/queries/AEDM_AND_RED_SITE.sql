SELECT p.username, o.name, s.name
FROM site s, organisation o, person p, organisation_business_role_map obrm,
(SELECT max(id), site_id, site_assessment_score FROM enforcement_site_assessment GROUP BY site_id) as esa
WHERE esa.site_assessment_score > 360
AND s.id = esa.site_id
AND o.id = s.organisation_id
AND obrm.organisation_id = o.id
AND obrm.business_role_id = 1
AND obrm.status_id = 1
AND p.id = obrm.person_id
AND p.username IS NOT NULL
GROUP BY s.id
LIMIT 10