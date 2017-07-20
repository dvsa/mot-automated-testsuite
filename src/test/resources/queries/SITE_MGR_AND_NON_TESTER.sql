select person_mgr.username as mgr_username,
   s.name as site_name, s.site_number as site_number,
   person_non_tester.username as non_tester_username,
   concat_ws(' ', person_non_tester.first_name,
     person_non_tester.middle_name, person_non_tester.family_name) as non_tester_name
from site s, person person_mgr,
  site_business_role_map sbrm_mgr,
  person person_non_tester, auth_for_testing_mot aftm
where s.id = sbrm_mgr.site_id
and sbrm_mgr.person_id = person_mgr.id
and sbrm_mgr.site_business_role_id = 2 -- site manager
and person_mgr.username is not null -- exclude dodgy test data
and person_non_tester.id = aftm.person_id
and aftm.status_id = 9 -- only qualified testers
and not exists (
  select 1 from site_business_role_map sbrm_non_tester
  where sbrm_non_tester.site_id = s.id
  and sbrm_non_tester.person_id = person_non_tester.id
  and sbrm_non_tester.site_business_role_id = 1 -- tester
)
and person_non_tester.username is not null -- exclude dodgy test data
and person_mgr.id != person_non_tester.id -- different user
limit 50