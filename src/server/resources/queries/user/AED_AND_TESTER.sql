select distinct (aed.username), ae.name, s.name, s.site_number, tester_person.username as tester_username,
  concat_ws(' ', tester_person.first_name, tester_person.middle_name, tester_person.family_name) as other_name
from person aed
left join person_security_card_map pscm on aed.id = pscm.person_id
left join security_card sc on sc.id = pscm.security_card_id
left join security_card_drift scd on sc.id = scd.security_card_id
right join organisation_business_role_map obrm on aed.id = obrm.person_id
right join organisation ae on obrm.organisation_id = ae.id
right join site s on s.organisation_id = ae.id
left join auth_for_testing_mot_at_site aftmas on s.id = aftmas.site_id
join person tester_person
left join auth_for_testing_mot aftm on tester_person.id = aftm.person_id
where aed.username is not null
and sc.security_card_status_lookup_id = 1 -- only assigned cards
and scd.last_observed_drift between -60 and 60 -- has security_card with acceptable drift
and obrm.business_role_id = 2
and obrm.status_id = 1 -- active
and ae.organisation_type_id = 7
and (select count(*) from site site_count where site_count.organisation_id = ae.id) = 1 -- Only has 1 site
and tester_person.username is not null
and aftm.status_id = 9 -- other user is qualified tester
and aftm.vehicle_class_id = aftmas.vehicle_class_id -- both site and person are authorised to test the same class
and aftm.vehicle_class_id = 4 -- Only look at class 4 for speed
and not exists (
	select 1
	from site_business_role_map sbrm
	where sbrm.site_id = s.id
	and sbrm.site_business_role_id = 1
	and tester_person.id = sbrm.person_id
) -- not already associated with the site
and not exists (
	select max(psrm.person_system_role_id) as max_role
	from person_system_role_map psrm
	where tester_person.id = psrm.person_id
	having max_role != 1
) -- removes dvsa users that can't be searched
and coalesce(trim(tester_person.middle_name), '') != ''  -- avoid name formatting issues on some screens
limit 10