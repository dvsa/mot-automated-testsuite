select veh.registration
from vehicle as veh
left join mot_test_current as mtc on veh.id = mtc.vehicle_id
inner join dvla_vehicle as dv on dv.vehicle_id = veh.id
and veh.registration not like "%-%" -- exclude dodgy test data on ACPT
and veh.registration is not null -- nullable in PP/Prod
and veh.vin is not null -- nullable in PP/Prod
and dv.first_registration_date < "1960-01-01"
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
and mtc.vehicle_id is null
limit 50