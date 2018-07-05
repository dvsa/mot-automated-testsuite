select veh.registration, date_format(t.issued_date,'%Y%m%d') as issued_date, t.number
from mot_test_current t
join vehicle veh on t.vehicle_id = veh.id
where
t.status_id = 6 -- only consider passed tests
and t.expiry_date > CURRENT_DATE -- that have not expired
and t.id = (
    select max(id) as id -- and only if latest test is the pass
    from mot_test_current mtc
    where mtc.vehicle_id = t.vehicle_id
    group by vehicle_id
)
and t.odometer_result_type = 'OK'
and veh.registration not like "%-%" -- exclude registration with hyphon - these are likely foreign plates
and veh.registration not like "@%" -- exclude deleted vehicles
and veh.registration is not null -- exclude null even though it's nullable
and veh.vin is not null -- exclude null even though it's nullable
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
limit 50;
