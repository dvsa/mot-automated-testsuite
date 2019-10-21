select veh.registration, concat(ma.name, ' ', mo.name) as make
from vehicle veh, model_detail md, model mo, make ma,
  (select max(id) as id, vehicle_id  from mot_test_current
   group by vehicle_id
   limit 100000) as latest_mot,
   mot_test_current mtc
where veh.model_detail_id = md.id
and md.vehicle_class_id = 4 -- cars only
and veh.id = latest_mot.vehicle_id
and md.model_id = mo.id
and mo.make_id = ma.id
and mtc.id = latest_mot.id
and mtc.status_id not in (4,5) -- exclude vehicles whose latest status is under test or failed
and odometer_result_type = 'OK'
AND mtc.mot_test_type_id = 1 -- Normal MOT test
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
and datediff(mtc.expiry_date, curdate()) = -1 -- mot expired yesterday
limit 10