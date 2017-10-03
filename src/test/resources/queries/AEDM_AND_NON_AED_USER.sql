select aedm_person.username as aedm_user, other_person.username as other_username, o.name,
  concat_ws(' ', other_person.first_name, other_person.middle_name, other_person.family_name) as other_name
from organisation o, organisation_business_role_map obrm, person aedm_person,
  person other_person, organisation_business_role_map other_obrm,
  security_card sc, person_security_card_map pscm
where obrm.business_role_id = 1 -- AEDM role
and obrm.status_id = 1 -- Active role
and o.id = obrm.organisation_id
and aedm_person.id = obrm.person_id
and aedm_person.id = pscm.person_id
and sc.id = pscm.security_card_id
and sc.security_card_status_lookup_id = 1 -- only assigned cards
and not exists ( -- not all security_card have a corresponding security_card_drift
  select 1 from security_card_drift scd
  where sc.id = scd.security_card_id
  and (scd.last_observed_drift > 60 or scd.last_observed_drift < -60) -- no drift beyond +/-2
)
and aedm_person.username is not null
and other_person.id not in (
    select person_id from organisation_business_role_map)
and not exists (
	select max(psrm.person_system_role_id) as max_role
	from person_system_role_map psrm
	where other_person.id = psrm.person_id
	having max_role != 1
) -- Removes DVSA users that can't be searched
and other_person.username is not null
and other_person.id != aedm_person.id
and coalesce(trim(other_person.middle_name), '') != ''  -- avoid name formatting issues on some screens
-- limit 20