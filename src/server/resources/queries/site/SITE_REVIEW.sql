SELECT DISTINCT s.site_number, s.name
FROM site s
  LEFT JOIN site_review sr ON sr.site_id = s.id
WHERE s.organisation_id IS NOT NULL
AND sr.site_id = s.id
AND sr.site_id NOT IN (
  SELECT sr.site_id
  FROM site_review sr
  WHERE sr.site_review_status_lookup_id = 1
)
LIMIT 30
