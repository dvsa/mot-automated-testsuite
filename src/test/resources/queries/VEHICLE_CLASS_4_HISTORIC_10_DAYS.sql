select veh.registration, veh.vin, mtc.odometer_value, mtc.number as number
from vehicle veh, model_detail md,
  (select count(vehicle_id) as test_count, min(id) as id, vehicle_id  from mot_test_current
   group by vehicle_id
   limit 10000) as oldest_mot,
   mot_test_current mtc
where veh.model_detail_id = md.id
and md.vehicle_class_id = 4 -- cars only
and veh.id = oldest_mot.vehicle_id
and mtc.id = oldest_mot.id
and oldest_mot.test_count > 1 -- More than one test for the vehicle
and mtc.status_id not in (4,5) -- exclude vehicles whose latest status is under test
and odometer_result_type = 'OK'
and veh.registration not like "%-%" -- exclude dodgy test data on ACPT
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
and mtc.issued_date < CURDATE() - 10 -- Oldest certificate older than 10 days
and mtc.last_updated_on < CURDATE() -- Certificate has not been updated today
limit 100