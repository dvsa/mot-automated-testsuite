select p.username, o.name, s.name
from site s, organisation o, person p, organisation_business_role_map obrm,
  security_card sc, person_security_card_map pscm, site_risk_score srs
where o.id = s.organisation_id
and o.id = srs.organisation_id
and srs.risk_colour = "RED"
and exists (
  select 1 from auth_for_testing_mot_at_site aft
  where aft.site_id = s.id
  and aft.vehicle_class_id in (3, 4, 5, 7) -- group B
)
and obrm.organisation_id = o.id
and obrm.business_role_id = 1
and obrm.status_id = 1
and p.id = obrm.person_id
and p.id = pscm.person_id
and sc.id = pscm.security_card_id
and sc.security_card_status_lookup_id = 1 -- only assigned cards
and not exists ( -- not all security_card have a corresponding security_card_drift
  select 1 from security_card_drift scd
  where sc.id = scd.security_card_id
  and (scd.last_observed_drift > 60 or scd.last_observed_drift < -60) -- no drift beyond +/-2
)
and p.username is not null
and exists (select 1 from site_business_role_map sbrm where sbrm.site_id = s.id and sbrm.site_business_role_id = 1) -- At least one tester exists
and (select count(*) from site site_count where site_count.organisation_id = o.id) < 9 -- Has less than 9 sites else we see a paginated list
group by s.id
limit 10;