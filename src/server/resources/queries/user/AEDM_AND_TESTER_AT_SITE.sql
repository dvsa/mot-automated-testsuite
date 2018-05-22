select distinct aedm_person.username as aedm_username, o.name as ae_name,
  s.name as site_name, s.site_number as site_number,
  tester_person.username as tester_username,
  concat_ws(' ', tester_person.first_name, tester_person.middle_name, tester_person.family_name) as tester_name
from person aedm_person, organisation o, site s, person tester_person, site_business_role_map sbrm,
  organisation_business_role_map obrm, mot_test_current mtc,
  auth_for_testing_mot aftm, auth_for_testing_mot_at_site afts, auth_for_ae afa,
  security_card sc, person_security_card_map pscm
where obrm.business_role_id = 1
and obrm.status_id =1
and aedm_person.id = obrm.person_id
and aedm_person.is_account_claim_required = 0
and aedm_person.is_password_change_required = 0
and not exists (select 1 from site_business_role_map sbrm2
                where sbrm2.person_id = aedm_person.id
                and sbrm2.site_business_role_id = 1) -- Who is not a tester means tester will be different user to AEDM
and o.id = obrm.organisation_id
and o.slots_balance > 0
and afa.organisation_id = o.id
and afa.status_id = 2
and s.organisation_id = o.id
and afts.site_id = s.id
and afts.vehicle_class_id = 4
and afts.status_id = 2
and sbrm.site_id = s.id
and sbrm.site_business_role_id = 1
and sbrm.status_id = 1
and tester_person.id = sbrm.person_id
and tester_person.is_account_claim_required = 0
and tester_person.is_password_change_required = 0
and aftm.person_id = tester_person.id
and aftm.vehicle_class_id = 4
and aftm.status_id = 9
and aedm_person.id = pscm.person_id
and sc.id = pscm.security_card_id
and sc.security_card_status_lookup_id = 1 -- only assigned cards
and not exists ( -- not all security_card have a corresponding security_card_drift
  select 1 from security_card_drift scd
  where sc.id = scd.security_card_id
  and (scd.last_observed_drift > 60 or scd.last_observed_drift < -60) -- no drift beyond +/-2
)
and mtc.person_id = tester_person.id
and not exists (
  select 1 from mot_test_current mtc
  where tester_person.id = mtc.last_updated_by
  and mtc.status_id = 4 -- exclude any testers with active tests
)
and (select count(*) from site site_count where site_count.organisation_id = o.id) < 5 -- Has less than 5 sites else we see a different link
and aedm_person.username is not null
and tester_person.username is not null
and coalesce(trim(tester_person.middle_name), '') != ''  -- avoid name formatting issues on some screens
limit 20