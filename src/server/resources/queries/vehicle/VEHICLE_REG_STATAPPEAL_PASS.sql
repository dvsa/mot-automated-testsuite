select veh.registration, left(mtc.number,4) as testno
from vehicle veh, mot_test_current mtc
where veh.registration is not null
and mtc.vehicle_id = veh.id
and mtc.mot_test_type_id = 7
and mtc.status_id = 6
limit 10