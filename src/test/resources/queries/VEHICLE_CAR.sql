select veh.registration, veh.vin, mtc.odometer_value
from vehicle veh, model_detail md, model, make, (
  select max(id) as id, vehicle_id  from mot_test_current
  group by vehicle_id) as latest_mot, mot_test_current mtc
where veh.model_detail_id = md.id
and md.model_id = model.id
and model.name is not null
and model.make_id = make.id
and model.name is not null
and md.vehicle_class_id = 4 -- cars only
and veh.id = latest_mot.vehicle_id
and mtc.id = latest_mot.id
and mtc.odometer_result_type = 'OK'
and mtc.status_id = 6 -- only previous mot test passes (not active or failed etc)