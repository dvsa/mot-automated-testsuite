select veh.registration, mtc.number, adr.address_line_1, adr.address_line_2
from vehicle veh, address adr, contact_detail cd, site_contact_detail_map scdm, site s, mot_test_current mtc
where veh.registration is not null
and mtc.vehicle_id = veh.id
and s.id = mtc.site_id
and scdm.site_id = s.id
and cd.id = scdm.contact_detail_id
and adr.id = cd.address_id
and CHAR_LENGTH(adr.address_line_1) = 50
and CHAR_LENGTH(adr.address_line_2) = 50
and CHAR_LENGTH(adr.town) = 50
and CHAR_LENGTH(adr.postcode) = 10
limit 10