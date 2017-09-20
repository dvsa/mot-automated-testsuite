select p.username as username, o.name as organisation
from person p, organisation o, organisation_business_role_map obrm,
  security_card sc, person_security_card_map pscm, security_card_drift scd
where obrm.person_id = p.id
and obrm.business_role_id = 1 -- AEDM role id
and p.is_account_claim_required = 0
and p.is_password_change_required = 0
and p.id = pscm.person_id
and obrm.organisation_id = o.id
and sc.id = pscm.security_card_id
and sc.security_card_status_lookup_id = 1
and scd.security_card_id = pscm.security_card_id
and scd.last_observed_drift between -60 and 60
and p.username is not null -- exclude dodgy test data
limit 50