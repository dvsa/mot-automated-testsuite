SELECT aedm_person.username as aedm_user, other_person.username as other_username, o.name,
concat_ws(' ', other_person.first_name, other_person.middle_name, other_person.family_name) as other_name
FROM organisation o, organisation_business_role_map obrm, person aedm_person, person other_person, organisation_business_role_map other_obrm
WHERE obrm.business_role_id = 1 -- AEDM role
AND obrm.status_id = 1 -- Active role
AND o.id = obrm.organisation_id
AND aedm_person.id = obrm.person_id
AND aedm_person.username IS NOT NULL
AND other_person.id NOT IN (SELECT person_id FROM organisation_business_role_map)
AND NOT EXISTS (
	SELECT max(psrm.person_system_role_id) as max_role FROM person_system_role_map psrm
	WHERE other_person.id = psrm.person_id
	HAVING max_role != 1
) -- Removes DVSA users that can't be searched
AND other_person.username IS NOT NULL
AND other_person.id != aedm_person.id
LIMIT 20