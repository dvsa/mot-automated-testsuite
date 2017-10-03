select afe.ae_ref as ae_ref, o.name as ae_name, unassigned_tester.username as unassigned_tester,
  concat_ws(' ', unassigned_tester.first_name,
     unassigned_tester.middle_name, unassigned_tester.family_name) as unassigned_tester_name
from organisation o, auth_for_ae afe, person unassigned_tester, auth_for_testing_mot aftm
where o.name is not null
and afe.organisation_id = o.id
and not exists (
  select * from organisation_business_role_map obrm
  where obrm.organisation_id = o.id
  and business_role_id = 1 -- AEDM
)
and unassigned_tester.username is not null
and not exists (
  select * from organisation_business_role_map obrm
  where obrm.organisation_id = o.id
  and obrm.person_id = unassigned_tester.id
)
and aftm.person_id = unassigned_tester.id
and aftm.vehicle_class_id = 4 -- only cars
and aftm.status_id = 9 -- only qualified testing authorisations
and coalesce(trim(unassigned_tester.middle_name), '') != ''  -- avoid name formatting issues on some screens
-- limit 10