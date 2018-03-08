select distinct veh.registration, concat(ma.name, ' ', mo.name) as make
from vehicle veh, model_detail md, model mo, make ma, mot_test_current mtc
where veh.model_detail_id = md.id
and md.vehicle_class_id = 4 -- cars only
and md.model_id = mo.id
and mo.make_id = ma.id
and mtc.vehicle_id = veh.id
and mtc.odometer_result_type = 'NO_METER' -- odometer not present
and veh.registration not like "%-%" -- exclude dodgy test data on ACPT
and veh.registration is not null -- nullable in PP/Prod
and veh.vin is not null -- nullable in PP/Prod
and not exists (
    select 1 from vehicle v
    where v.registration = veh.registration
    group by v.registration
    having count(v.registration) > 1 -- exclude where same registration has been entered as different vehicles
)
and not exists (
    select 1 from vehicle v
    where v.vin = veh.vin
    group by v.vin
    having count(v.vin) > 1 -- exclude where same vin has been entered as different vehicles
)
limit 10