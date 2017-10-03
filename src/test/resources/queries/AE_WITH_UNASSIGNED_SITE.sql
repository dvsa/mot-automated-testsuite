select ae_ref, ae_name, site_number, site_name from (
    select afe.ae_ref as ae_ref, o.name as ae_name, unassigned_site.site_number as site_number,
      unassigned_site.name as site_name
    from organisation o, auth_for_ae afe, site unassigned_site
    where o.name is not null
    and afe.organisation_id = o.id
    and unassigned_site.site_status_id = 1 -- approved site
    and not exists (
      select * from organisation_site_map osm
      where osm.organisation_id = o.id
      and osm.site_id = unassigned_site.id
    )
    and unassigned_site.name not like 'VOSA %'
    and unassigned_site.name not like 'DVSA %'
    limit 100000) o
group by o.ae_ref
-- limit 10