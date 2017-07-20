select person_mgr.username as mgr_username,
   s.name as site_name, s.site_number as site_number,
   person_non_admin.username as non_admin_username,
   concat_ws(' ', person_non_admin.first_name,
     person_non_admin.middle_name, person_non_admin.family_name) as non_admin_name
from site s, person person_mgr,
  site_business_role_map sbrm_mgr,
  person person_non_admin
where s.id = sbrm_mgr.site_id
and sbrm_mgr.person_id = person_mgr.id
and sbrm_mgr.site_business_role_id = 2 -- site manager
and person_mgr.username is not null -- exclude dodgy test data
and not exists (
  select 1 from site_business_role_map sbrm_non_admin
  where sbrm_non_admin.site_id = s.id
  and sbrm_non_admin.person_id = person_non_admin.id
  and sbrm_non_admin.site_business_role_id = 3 -- site admin
)
and person_non_admin.username is not null -- exclude dodgy test data
and person_mgr.id != person_non_admin.id -- different user
limit 50