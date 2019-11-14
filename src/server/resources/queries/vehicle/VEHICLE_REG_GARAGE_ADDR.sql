select veh.registration, concat(left(mtc.number,4), ' ', substring(mtc.number,5,4), ' ', right(mtc.number,4)) as testno, adr.address_line_1, adr.address_line_2, s.name, dvla.recent_v5_document_number
from vehicle veh, address adr, contact_detail cd, site_contact_detail_map scdm, site s, mot_test_current mtc, dvla_vehicle dvla
where veh.registration is not null
and mtc.vehicle_id = veh.id
and veh.dvla_vehicle_id = dvla.dvla_vehicle_id
and s.id = mtc.site_id
and scdm.site_id = s.id
and cd.id = scdm.contact_detail_id
and adr.id = cd.address_id
and mtc.mot_test_type_id = 1
and s.name != 'Example Site' -- removes test data
and adr.address_line_1 is not null
and adr.address_line_2 is not null
and adr.address_line_1 not like "%  %" -- excludes invalid data containing to spaces
and adr.address_line_1 not like "%  %" -- excludes invalid data containing to spaces
limit 2