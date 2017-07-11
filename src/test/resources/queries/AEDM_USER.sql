SELECT p.username as username, o.name as organisation
FROM person p, organisation o, organisation_business_role_map obrm,
security_card sc, person_security_card_map pscm
WHERE obrm.person_id = p.id
AND obrm.business_role_id = 1 -- AEDM role id
AND p.is_account_claim_required = 0
AND p.is_password_change_required = 0
AND p.id = pscm.person_id
AND obrm.organisation_id = o.id
AND sc.id = pscm.security_card_id
AND sc.security_card_status_lookup_id = 1
and p.username is not null -- exclude dodgy test data
limit 50