select p.username, s.name, s.site_number
from site s, organisation o, person p, organisation_business_role_map obrm,
  security_card sc, person_security_card_map pscm
where o.id = s.organisation_id
and exists (
  select 1 from auth_for_testing_mot_at_site aft
  where aft.site_id = s.id
  and aft.vehicle_class_id in (1, 2) -- group A
)
and obrm.organisation_id = o.id
and obrm.business_role_id = 2 -- AED user
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
-- limit 10