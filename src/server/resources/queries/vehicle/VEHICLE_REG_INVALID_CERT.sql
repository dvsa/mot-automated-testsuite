select veh.registration, concat(left(mtc.number,4), ' ', substring(mtc.number,5,4), ' ', right(mtc.number,4)) as testno, dvla.recent_v5_document_number
from vehicle veh, mot_test_current mtc, dvla_vehicle dvla
where veh.registration is not null
and mtc.vehicle_id = veh.id
and mtc.document_id is NULL
and veh.dvla_vehicle_id = dvla.dvla_vehicle_id
and mtc.mot_test_type_id = 1
and mtc.completed_date > str_to_date('20/05/2018', '%d/%m/%Y') -- completed mot tests after EURW changes
and dvla.registration = veh.registration
limit 1;