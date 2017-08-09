SELECT DISTINCT aed_person.username, o.name as organisation, s.name as site, tester_person.username as tester_username,
concat_ws(' ', tester_person.first_name, tester_person.middle_name, tester_person.family_name) as other_name
FROM person aed_person, organisation o, site s, person tester_person, organisation_business_role_map obrm,
auth_for_testing_mot aftm
WHERE obrm.business_role_id = 2
AND obrm.status_id = 1 -- ACtive
AND o.id = obrm.organisation_id
AND o.organisation_type_id = 7
AND aed_person.id = obrm.person_id
AND aed_person.username IS NOT NULL
AND s.organisation_id = o.id
and aftm.status_id = 9 -- other user is qualified tester
and tester_person.id = aftm.person_id
AND NOT EXISTS (
	SELECT tester_person.id FROM site_business_role_map sbrm
	WHERE sbrm.site_id = s.id
	AND sbrm.site_business_role_id = 1
	AND tester_person.id = sbrm.person_id
)
AND NOT EXISTS (
	SELECT max(psrm.person_system_role_id) as max_role FROM person_system_role_map psrm
	WHERE tester_person.id = psrm.person_id
	HAVING max_role != 1
) -- Removes DVSA users that can't be searched
AND tester_person.username IS NOT NULL
AND tester_person.id != aed_person.id
LIMIT 10