select veh.registration, concat(left(mtc.number,4), ' ', substring(mtc.number,5,4), ' ', right(mtc.number,4)) as testno
from vehicle veh, mot_test_current mtc
where veh.registration is not null
and mtc.vehicle_id = veh.id
and mtc.mot_test_type_id = 6 -- inverted appeal
and mtc.status_id = 5 -- status fail
limit 10