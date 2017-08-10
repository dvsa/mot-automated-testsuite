SELECT DISTINCT aedm_person.username, o.name, s.name, tester_person.username
FROM person aedm_person, organisation o, site s, person tester_person, site_business_role_map sbrm, organisation_business_role_map obrm, mot_test_current mtc,
auth_for_testing_mot aftm, auth_for_testing_mot_at_site afts, auth_for_ae afa
WHERE obrm.business_role_id = 1
AND obrm.status_id =1
AND aedm_person.id = obrm.person_id
AND aedm_person.is_account_claim_required = 0
AND aedm_person.is_password_change_required = 0
AND o.id = obrm.organisation_id
AND o.slots_balance > 0
AND afa.organisation_id = o.id
AND afa.status_id = 2
AND s.organisation_id = o.id
AND afts.site_id = s.id
AND afts.vehicle_class_id = 4
AND afts.status_id = 2
AND sbrm.site_id = s.id
AND sbrm.site_business_role_id = 1
AND sbrm.status_id = 1
AND tester_person.id = sbrm.person_id
AND tester_person.is_account_claim_required = 0
AND tester_person.is_password_change_required = 0
AND aftm.person_id = tester_person.id
AND aftm.vehicle_class_id = 4
AND aftm.status_id = 9
AND mtc.person_id = tester_person.id
and not exists (
  select 1 from mot_test_current mtc
  where tester_person.id = mtc.last_updated_by
  and mtc.status_id = 4 -- exclude any testers with active tests
)
AND aedm_person.username IS NOT NULL
AND tester_person.username IS NOT NULL
LIMIT 10