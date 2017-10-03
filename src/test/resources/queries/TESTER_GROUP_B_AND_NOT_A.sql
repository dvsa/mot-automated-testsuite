select p.username as username,
  concat_ws(' ', p.first_name, p.middle_name, p.family_name) as name
from person p
where not exists (
  select 1 from auth_for_testing_mot aftm
  where aftm.person_id = p.id
  and aftm.vehicle_class_id = 1 -- vehicle class 1 (A)
  and aftm.status_id in (7, 8, 9, 10) -- needs training, qualified, etc
)
and not exists (
  select 1 from auth_for_testing_mot aftm
  where aftm.person_id = p.id
  and aftm.vehicle_class_id = 2 -- vehicle class 2 (A)
  and aftm.status_id in (7, 8, 9, 10) -- needs training, qualified, etc
)
and exists (
  select 1 from auth_for_testing_mot aftm
  where aftm.person_id = p.id
  and aftm.vehicle_class_id = 3 -- vehicle class 3 (B)
  and aftm.status_id = 9 -- qualified
)
and exists (
  select 1 from auth_for_testing_mot aftm
  where aftm.person_id = p.id
  and aftm.vehicle_class_id = 4 -- vehicle class 4 (B)
  and aftm.status_id = 9 -- qualified
)
and exists (
  select 1 from auth_for_testing_mot aftm
  where aftm.person_id = p.id
  and aftm.vehicle_class_id = 5 -- vehicle class 5 (B)
  and aftm.status_id = 9 -- qualified
)
and exists (
  select 1 from auth_for_testing_mot aftm
  where aftm.person_id = p.id
  and aftm.vehicle_class_id = 7 -- vehicle class 7 (B)
  and aftm.status_id = 9 -- qualified
)
and p.username is not null -- exclude dodgy test data
and coalesce(trim(p.middle_name), '') != ''  -- avoid name formatting issues on some screens
-- limit 10
