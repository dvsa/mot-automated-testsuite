SELECT DISTINCT s.site_number, s.name, o.name
FROM site s, site_review sr, organisation o
WHERE s.organisation_id IS NOT NULL
AND s.organisation_id = o.id
AND s.id NOT IN (
  SELECT sr.site_id
  FROM site_review sr
)
LIMIT 30
