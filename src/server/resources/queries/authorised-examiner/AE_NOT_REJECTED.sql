select afe.ae_ref as ae_ref, o.name as ae_name
from organisation o, auth_for_ae afe
where o.name is not null
and afe.organisation_id = o.id
and afe.status_id != 6 -- AE is not rejected status
and afe.ao_site_id != 8 -- not DVSA Area Office 08
limit 10