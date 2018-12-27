SELECT DISTINCT s.site_number, s.name
FROM site s, site_review sr
WHERE s.organisation_id IS NOT NULL
AND s.id NOT IN (
  SELECT sr.site_id
  FROM site_review sr
)
LIMIT 30
