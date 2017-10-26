select afe.ae_ref as ae_ref, o.name as ae_name, assigned_site.site_number as site_number,
  assigned_site.name as site_name
from organisation o, auth_for_ae afe, site assigned_site, organisation_site_map osm
where o.name is not null
and afe.organisation_id = o.id
and osm.organisation_id = o.id
and osm.site_id = assigned_site.id
and osm.status_id = 2 -- active site association
and assigned_site.name not like 'VOSA %'
and assigned_site.name not like 'DVSA %'
limit 10