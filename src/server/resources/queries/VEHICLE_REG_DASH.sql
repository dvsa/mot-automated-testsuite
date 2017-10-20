select veh.registration, concat(ma.name, ' ', mo.name) as make
from vehicle veh, model_detail md, model mo, make ma, mot_test_current mtc
where veh.id < 10000000
and veh.registration like '%-%'
and veh.model_detail_id = md.id
and md.model_id = mo.id
and mo.make_id = ma.id
and mtc.vehicle_id = veh.id
and mtc.status_id in (5,6) -- mot test is passed or failed
and mtc.mot_test_type_id = 1 -- normal test
limit 10