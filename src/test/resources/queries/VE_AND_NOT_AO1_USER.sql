select p.username as username,
  concat_ws(' ', p.first_name, p.middle_name, p.family_name) as name
from person p, person_system_role_map role_map
where p.id = role_map.person_id
and role_map.person_system_role_id = 2 -- Vehicle Examiner (VE) role
and not exists (
  select 1 from person_system_role_map psrm
  where p.id = psrm.person_id
  and psrm.person_system_role_id = 5 -- and not Area Office 1 (AO1) role
)
and p.username is not null
and coalesce(trim(p.middle_name), '') != ''  -- avoid name formatting issues on some screens
group by p.username
limit 20