select veh.registration, veh.vin, mtc.odometer_value
from vehicle veh, model_detail md,
  (select max(id) as id, vehicle_id  from mot_test_current
   group by vehicle_id
   limit 100000) as latest_mot,
   mot_test_current mtc
where veh.model_detail_id = md.id
and md.vehicle_class_id = 4 -- cars only
and veh.id = latest_mot.vehicle_id
and mtc.id = latest_mot.id
and mtc.status_id not in (4,5) -- exclude vehicles whose latest status is under test or failed
and datediff(curdate(), mtc.completed_date) > 15 -- excludes retests, tests that are over 8 days old
and mtc.completed_date is not null -- excludes first-time tests
and odometer_result_type = 'OK'
and veh.registration <> 'R3GHAU5' -- Exclude vehicles that have already been modified by automation
and veh.registration <> 'R3GHA01' -- Exclude vehicles that have already been modified by automation
and veh.registration <> 'R3GHDVL5' -- Exclude vehicles that have already been modified by automation
and veh.registration <> 'DVLA903' -- Exclude vehicles that have already been modified by automation
and veh.registration <> 'DVLA904' -- Exclude vehicles that have already been modified by automation
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
and veh.first_used_date >= str_to_date('01/09/2010', '%d/%m/%Y') -- vehicle first used on or after 01/09/2010
limit 50