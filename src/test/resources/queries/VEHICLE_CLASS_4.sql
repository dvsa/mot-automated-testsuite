select veh.registration, veh.vin, mtc.odometer_value
from vehicle veh, model_detail md,
  (select max(id) as id, vehicle_id  from mot_test_current
   where status_id not in (4,5) -- exclude vehicles in active test or failed
   and odometer_result_type = 'OK'
   group by vehicle_id
   limit 200) as latest_mot,
   mot_test_current mtc
where veh.model_detail_id = md.id
and md.vehicle_class_id = 4 -- cars only
and veh.id = latest_mot.vehicle_id
and mtc.id = latest_mot.id
and veh.registration not like "%-%" -- exclude dodgy test data on ACPT
limit 100