select p.username as username, s.name as site_name, s.site_number as site_number
from site s, person p, site_business_role_map sbrm
where s.id = sbrm.site_id
and sbrm.person_id = p.id
and sbrm.site_business_role_id = 2 -- site mgr
and sbrm.status_id = 1 -- site mgr role accepted
and p.username is not null -- exclude dodgy test data
limit 10