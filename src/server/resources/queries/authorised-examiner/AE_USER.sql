select DISTINCT afe.ae_ref as ae_ref, o.name as ae_name, o.`slots_balance`
from organisation o, auth_for_ae afe, person aedm_user, organisation_business_role_map obrm
where o.name is not null
and afe.organisation_id = o.id
and obrm.organisation_id = o.id
and aedm_user.username is not null
and obrm.person_id = aedm_user.id
order by RAND()
limit 20