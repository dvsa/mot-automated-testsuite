select distinct aed_person.username, o.name as organisation,
  s.name as siteName, s.site_number as siteNumber, tester_person.username as tester_username,
  concat_ws(' ', tester_person.first_name, tester_person.middle_name, tester_person.family_name) as other_name
from person aed_person, organisation o, site s, person tester_person, organisation_business_role_map obrm,
  auth_for_testing_mot aftm, security_card sc, person_security_card_map pscm
where obrm.business_role_id = 2
and obrm.status_id = 1 -- active
and o.id = obrm.organisation_id
and o.organisation_type_id = 7
and aed_person.id = obrm.person_id
and aed_person.username is not null
and s.organisation_id = o.id
and aftm.status_id = 9 -- other user is qualified tester
and aed_person.id = pscm.person_id
and sc.id = pscm.security_card_id
and sc.security_card_status_lookup_id = 1 -- only assigned cards
and not exists ( -- not all security_card have a corresponding security_card_drift
  select 1 from security_card_drift scd
  where sc.id = scd.security_card_id
  and (scd.last_observed_drift > 60 or scd.last_observed_drift < -60) -- no drift beyond +/-2
)
and tester_person.id = aftm.person_id
and not exists (
	select 1
	from site_business_role_map sbrm
	where sbrm.site_id = s.id
	and sbrm.site_business_role_id = 1
	and tester_person.id = sbrm.person_id
)
and not exists (
	select max(psrm.person_system_role_id) as max_role
	from person_system_role_map psrm
	where tester_person.id = psrm.person_id
	having max_role != 1
) -- removes dvsa users that can't be searched
and tester_person.username is not null
and tester_person.id != aed_person.id
and coalesce(trim(tester_person.middle_name), '') != ''  -- avoid name formatting issues on some screens
-- limit 10