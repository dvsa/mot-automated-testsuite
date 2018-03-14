select veh.registration, date_format(mtc.issued_date,'%Y%m%d') as issued_date, mtc.number
from vehicle veh, model_detail md,
  (select max(id) as id, vehicle_id  from mot_test_current
   group by vehicle_id
   limit 100000) as latest_mot,
   mot_test_current mtc,
   mot_test_emergency_reason as mter
where veh.model_detail_id = md.id
and mter.id = mtc.id
and veh.id = latest_mot.vehicle_id
and mtc.id = latest_mot.id
and mtc.status_id not in (4,5) -- exclude vehicles whose latest status is under test or failed
and date_add(mtc.issued_date, interval 31 day) > curdate()
and odometer_result_type = 'OK'
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
group by mtc.issued_date asc
limit 50