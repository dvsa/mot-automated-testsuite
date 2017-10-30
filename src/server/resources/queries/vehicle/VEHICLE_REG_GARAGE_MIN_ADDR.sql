select veh.registration, mtc.number, adr.address_line_1, adr.address_line_2
from vehicle veh, address adr, contact_detail cd, site_contact_detail_map scdm, site s, mot_test_current mtc
where veh.registration is not null
and mtc.vehicle_id = veh.id
and s.id = mtc.site_id
and scdm.site_id = s.id
and cd.id = scdm.contact_detail_id
and adr.id = cd.address_id
and CHAR_LENGTH(adr.address_line_1) = 1  -- there is no matching data in pp
and CHAR_LENGTH(adr.address_line_2) = 1
limit 10