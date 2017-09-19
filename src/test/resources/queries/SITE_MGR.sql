select p.username as username, s.name as site_name, s.site_number as site_number
from site s, person p, site_business_role_map sbrm, person_security_card_map pscm, security_card_drift scd
where s.id = sbrm.site_id
and sbrm.person_id = p.id
and sbrm.site_business_role_id = 2 -- site mgr
and sbrm.status_id = 1 -- site mgr role accepted
and p.username is not null -- exclude dodgy test data
and p.id = pscm.person_id
and scd.security_card_id = pscm.security_card_id
and scd.last_observed_drift between -60 and 60
limit 10