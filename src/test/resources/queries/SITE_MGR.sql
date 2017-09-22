select p.username as username, s.name as site_name, s.site_number as site_number
from site s, person p, site_business_role_map sbrm, security_card sc, person_security_card_map pscm
where s.id = sbrm.site_id
and sbrm.person_id = p.id
and sbrm.site_business_role_id = 2 -- site mgr
and sbrm.status_id = 1 -- site mgr role accepted
and p.username is not null -- exclude dodgy test data
and p.id = pscm.person_id
and sc.id = pscm.security_card_id
and sc.security_card_status_lookup_id = 1 -- only assigned cards
and not exists ( -- not all security_card have a corresponding security_card_drift
  select 1 from security_card_drift scd
  where sc.id = scd.security_card_id
  and (scd.last_observed_drift > 60 or scd.last_observed_drift < -60) -- no drift beyond +/-2
)
limit 10