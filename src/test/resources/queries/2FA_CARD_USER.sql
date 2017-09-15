select p.username as username, cast(scd.last_observed_drift/30 as decimal(0)) as last_drift
from person p, security_card sc, person_security_card_map pscm, security_card_drift scd
where p.id = pscm.person_id
and sc.id = pscm.security_card_id
and sc.security_card_status_lookup_id = 1 -- only assigned cards
and sc.id = scd.security_card_id
and p.username is not null -- exclude dodgy test data
limit 50
