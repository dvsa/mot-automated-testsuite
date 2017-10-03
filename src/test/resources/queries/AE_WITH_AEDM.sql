select afe.ae_ref as ae_ref, o.name as ae_name, aedm_user.username as aedm_username,
  concat_ws(' ', aedm_user.first_name,
     aedm_user.middle_name, aedm_user.family_name) as aedm_name
from organisation o, auth_for_ae afe, person aedm_user, organisation_business_role_map obrm
where o.name is not null
and afe.organisation_id = o.id
and obrm.organisation_id = o.id
and obrm.business_role_id = 1 -- AEDM
and obrm.status_id = 1 -- approved only
and aedm_user.username is not null
and obrm.person_id = aedm_user.id
and coalesce(trim(aedm_user.middle_name), '') != ''  -- avoid name formatting issues on some screens
-- limit 10