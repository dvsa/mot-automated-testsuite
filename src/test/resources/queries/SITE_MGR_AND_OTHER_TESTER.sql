select distinct(person_mgr.username) as mgr_username,
   s.name as site_name, s.site_number as site_number,
   person_other.username as other_username,
   concat_ws(' ', person_other.first_name,
     person_other.middle_name, person_other.family_name) as other_name
from site s, person person_mgr,
  site_business_role_map sbrm_mgr,
  person person_other, auth_for_testing_mot aftm
where s.id = sbrm_mgr.site_id
and sbrm_mgr.person_id = person_mgr.id
and sbrm_mgr.site_business_role_id = 2 -- site manager
and sbrm_mgr.status_id = 1 -- site manager role accepted
and person_mgr.username is not null -- exclude dodgy test data
and person_other.id = aftm.person_id
and aftm.status_id = 9 -- other user is qualified tester
and not exists ( -- other user not assoc with site as any role (pending or otherwise)
  select 1 from site_business_role_map sbrm_other
  where sbrm_other.site_id = s.id
  and sbrm_other.person_id = person_other.id
)
and person_other.username is not null -- exclude dodgy test data
and person_mgr.id != person_other.id -- different user
limit 50