SELECT p.username, o.name, s.name
FROM site s, organisation o, person p, organisation_business_role_map obrm,
  (SELECT max(id) as id, site_id FROM enforcement_site_assessment GROUP BY site_id) as latest_assessment,
  enforcement_site_assessment esa
WHERE latest_assessment.id = esa.id
AND latest_assessment.site_id = s.id
AND esa.id = latest_assessment.id
AND esa.site_assessment_score > 360.00
AND o.id = s.organisation_id
AND obrm.organisation_id = o.id
AND obrm.business_role_id = 1
AND obrm.status_id = 1
AND p.id = obrm.person_id
AND p.username IS NOT NULL
GROUP BY s.id
ORDER BY esa.site_assessment_score DESC
LIMIT 10