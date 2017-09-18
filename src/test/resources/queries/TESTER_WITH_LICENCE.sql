select p.username as username,
  concat_ws(' ', p.first_name, p.middle_name, p.family_name) as name,
  l.licence_number
from person p, licence l
where exists (
  select 1 from auth_for_testing_mot aftm
  where aftm.person_id = p.id
  and aftm.status_id = 9 -- qualified, for any vehicle class
)
and p.username is not null -- exclude dodgy test data
and p.driving_licence_id = l.id
and l.licence_type_id = 1 -- driving licence
and l.licence_country_id = 11 -- issued by GB
and p.middle_name is not null -- avoid name formatting issues on user search result screen
and p.middle_name not like '% %'
limit 10
