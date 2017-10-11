select p.username as username, o.name as organisation, count(tst.id) as transaction_count
from person p, organisation o, organisation_business_role_map obrm,
  security_card sc, person_security_card_map pscm, test_slot_transaction as tst
where obrm.person_id = p.id
and obrm.business_role_id = 1 -- AEDM role id
and p.is_account_claim_required = 0
and p.is_password_change_required = 0
and obrm.organisation_id = o.id
and p.id = pscm.person_id
and sc.id = pscm.security_card_id
and sc.security_card_status_lookup_id = 1 -- only assigned cards
and not exists ( -- not all security_card have a corresponding security_card_drift
  select 1 from security_card_drift scd
  where sc.id = scd.security_card_id
  and (scd.last_observed_drift > 60 or scd.last_observed_drift < -60) -- no drift beyond +/-2
)
and tst.organisation_id = o.id
and p.username is not null
and tst.last_updated_on > "2010-01-01 00:00:00.000000"
order by transaction_count desc
limit 50